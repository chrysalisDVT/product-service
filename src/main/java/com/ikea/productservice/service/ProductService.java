package com.ikea.productservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ikea.productservice.client.ProductServiceClient;
import com.ikea.productservice.constants.ProductExceptionCode;
import com.ikea.productservice.entities.Article;
import com.ikea.productservice.entities.Product;
import com.ikea.productservice.exception.ProductClientException;
import com.ikea.productservice.exception.ProductServiceException;
import com.ikea.productservice.repository.ProductRepository;
import com.ikea.productservice.utility.MapBeans;
import com.ikea.productservice.vo.ArticleVO;
import com.ikea.productservice.vo.ProductServiceVO;
import com.ikea.productservice.vo.ProductVO;

import reactor.core.publisher.Mono;

/**
 * Service layer for PRoduct apis
 * 
 * @author Dharmvir Tiwari
 *
 */

@Service
public class ProductService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

	private ProductRepository prodRepository;

	private ProductServiceClient productClient;

	public ProductService(ProductRepository prodRepository, ProductServiceClient productClient) {
		this.prodRepository = prodRepository;
		this.productClient = productClient;
	}

	/**
	 * Persists product information, if the product is already available, it throws
	 * ProductServiceException with BAD_REQUEST When the product is added, the
	 * update is cascaded to the inventory by adding extra stocks
	 * 
	 * @param productInfo
	 * @return
	 */
	@Transactional
	public ProductVO saveProductInformation(ProductVO productInfo) {
		LOG.info(productInfo.toString());
		try {

			Product productToSave = MapBeans.mapData(productInfo, Product.class);
			productToSave.setArticles(productInfo
					.getArticles().stream().map(article -> Article.builder().artId(article.getArtId())
							.amountOf(article.getAmountOf()).product(productToSave).build())
					.collect(Collectors.toList()));
			prodRepository.save(productToSave);
			LOG.info("Product information persisted:{}....Updating warehouse",
					prodRepository.findByName(productToSave.getName()));
			productClient.addWarehouseInventory(ProductServiceVO.builder().articles(productInfo.getArticles()).build())
					.subscribe();
			return productInfo;
		} catch (Exception ex) {
			throw new ProductServiceException(ProductExceptionCode.INTERNAL_SERVER_ERROR,
					"Error occurred while saving product details, please contact admin");
		}

	}

	/**
	 * Retrieves product information and throws ProductServiceException with
	 * INTERNAL_SERVER_ERROR in case there are issues while retrieving the
	 * information
	 * 
	 * @return
	 */
	public ProductServiceVO retrieveProductInformation() {
		try {
			List<ProductVO> productAvailable = prodRepository.findAll().stream().map(product -> {
				List<ArticleVO> articleList = product.getArticles().stream()
						.map(article -> new ArticleVO(article.getArtId(), article.getAmountOf()))
						.collect(Collectors.toList());
				ProductVO productInfo = new ProductVO();
				productInfo.setArticles(articleList);
				productInfo.setName(product.getName());
				return productInfo;
			}).collect(Collectors.toList());
			ProductServiceVO productResponse = new ProductServiceVO();
			productResponse.setProducts(productAvailable);
			LOG.info("Product information retrieved:{}", productResponse);
			return productResponse;
		} catch (Exception ex) {
			throw new ProductServiceException(ProductExceptionCode.INTERNAL_SERVER_ERROR,
					"Error occurred while processing your request, please cont");
		}
	}

	/**
	 * Removes the product from the catalogue and cascades the update to inventory
	 * stock by invoking the warehouse service
	 * 
	 * @param name
	 * @return
	 */

	@Transactional
	public Mono<ProductVO> sellProduct(String name) {
		try {
			// Wrapper for modifying the artList from based on the optional we receive from
			// the repository
			var wrapper = new Object() {
				List<ArticleVO> artIds = new ArrayList<>();
			};
			prodRepository.findByName(name).ifPresentOrElse(prod -> {
				wrapper.artIds = fetchArticleInfoFromProduct(prod, article -> ArticleVO.builder()
						.amountOf(article.getAmountOf()).artId(article.getArtId()).build());
				prodRepository.deleteByName(name);
			}, () -> {

				throw new ProductServiceException(ProductExceptionCode.BAD_REQUEST,
						"The information provided is not valid");
			});
			Mono<ProductVO> inventoryUpdate = productClient
					.updateWarehouseInventory(ProductServiceVO.builder().articles(wrapper.artIds).build());
			return inventoryUpdate.onErrorMap(ProductClientException.class, (exception) -> {
				LOG.info("Error occurred while updating inventory");
				return exception;
			});
		} catch (NonTransientDataAccessException ex) {
			throw new ProductServiceException(ProductExceptionCode.BAD_REQUEST,
					"The information provided is not valid");
		} catch (ProductClientException ex) {
			throw ex;
		} catch (ProductServiceException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ProductServiceException(ProductExceptionCode.INTERNAL_SERVER_ERROR,
					"The information provided is not valid");
		}

	}

	private <T> List<T> fetchArticleInfoFromProduct(final Product product, Function<Article, T> mapper) {
		return product.getArticles().stream().map(mapper).collect(Collectors.toList());
	}
}
