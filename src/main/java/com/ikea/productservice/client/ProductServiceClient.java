package com.ikea.productservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ikea.productservice.constants.ProductExceptionCode;
import com.ikea.productservice.exception.ProductClientException;
import com.ikea.productservice.service.ProductService;
import com.ikea.productservice.vo.ProductServiceVO;
import com.ikea.productservice.vo.ProductVO;

import reactor.core.publisher.Mono;

@Component
public class ProductServiceClient {

	private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

	private WebClient webClient;
	@Value("${warehouse.service.uri}")
	private String wareHouseServiceUri;

	public ProductServiceClient(WebClient webClient) {
		this.webClient = webClient;
	}

	/**
	 * Updated the warehouse inventory when a product is sold
	 * 
	 * @param productDetails
	 * @return
	 */
	public Mono<ProductVO> updateWarehouseInventory(final ProductServiceVO productDetails) {
		try {
			Mono<ProductVO> prodVo = webClient.patch().uri(wareHouseServiceUri)
					.header("Content-Type", "application/json").bodyValue(productDetails).retrieve()
					.bodyToMono(ProductVO.class);
			return prodVo;
		} catch (Exception ex) {
			throw new ProductClientException(ProductExceptionCode.BAD_REQUEST, "Error while invoking api");
		}

	}

	/**
	 * Updates the warehouse inventory when a product is added
	 * 
	 * @param productDetails
	 * @return
	 */

	public Mono<ProductVO> addWarehouseInventory(final ProductServiceVO productDetails) {
		try {
			LOG.info("Updating warehouse inventory....");
			Mono<ProductVO> prodVo = webClient.post().uri(wareHouseServiceUri)
					.header("Content-Type", "application/json").bodyValue(productDetails).retrieve()
					.bodyToMono(ProductVO.class);
			return prodVo;
		} catch (Exception ex) {
			throw new ProductClientException(ProductExceptionCode.BAD_REQUEST, "Error while invoking api");
		}

	}

}
