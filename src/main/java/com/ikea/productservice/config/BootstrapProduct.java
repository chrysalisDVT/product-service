package com.ikea.productservice.config;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikea.productservice.constants.ProductExceptionCode;
import com.ikea.productservice.entities.Article;
import com.ikea.productservice.entities.Product;
import com.ikea.productservice.exception.ProductServiceException;
import com.ikea.productservice.repository.ProductRepository;
import com.ikea.productservice.vo.ProductVO;

/**
 * 
 * @author Dharmvir Tiwari
 *
 */

@Component
public class BootstrapProduct {

	private static final Logger LOG = LoggerFactory.getLogger(BootstrapProduct.class);

	private ProductRepository productRepo;

	@Value("${data.source.path}")
	String productSourcePath;

	public BootstrapProduct(ProductRepository productRepo) {
		this.productRepo = productRepo;
	}
	
	/**
	 * Parses the source file provided and updated the product information
	 */
	@PostConstruct
	public void setup() {
		try {
			var jsonFile = ResourceUtils.getFile("classpath:" + productSourcePath);
			ObjectMapper objectMapper = new ObjectMapper();
			String dataString = objectMapper.readTree(jsonFile).get("products").toString();
			LOG.info(dataString);
			Collection<ProductVO> productList = objectMapper.readValue(dataString,
					new TypeReference<Collection<ProductVO>>() {
					});
			List<Product> prodList=productList.stream().map(
				prodVo -> {
				Product productToSave =  new Product();
				productToSave.setName(prodVo.getName());
				productToSave.setArticles(prodVo
						.getArticles().stream().map(article -> Article.builder().artId(article.getArtId())
								.amountOf(article.getAmountOf()).product(productToSave).build())
						.collect(Collectors.toList()));
				return productToSave;
			}).collect(Collectors.toList());
			productRepo.saveAll(prodList);
			
			productList.forEach(System.out::println);

		} catch (IOException e) {
			throw new ProductServiceException(ProductExceptionCode.INTERNAL_SERVER_ERROR, "Exception occurred while bootstraping the project");
		}
	}

}
