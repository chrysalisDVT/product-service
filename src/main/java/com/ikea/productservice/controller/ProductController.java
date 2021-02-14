package com.ikea.productservice.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikea.productservice.service.ProductService;
import com.ikea.productservice.vo.ProductServiceVO;
import com.ikea.productservice.vo.ProductVO;

import reactor.core.publisher.Mono;


/**
 * Controller pertaining to product apis
 * 
 * 
 * @author Dharmvir Tiwari
 *
 */
@RestController
@RequestMapping("/product")
public class ProductController {

	private ProductService productService;

	ProductController(ProductService productService) {
		this.productService = productService;
	}

	/**
	 * Retrieves all the product information
	 * @return
	 */
	@GetMapping
	public ProductServiceVO getProducts() {
		return productService.retrieveProductInformation();
	}
	
	/**
	 * Saves a new product
	 * @param productVo
	 * @return
	 */
	@PostMapping
	public Mono<ProductServiceVO> saveProduct(@RequestBody ProductVO productVo) {
			return Mono.just(ProductServiceVO.builder().product(productService.saveProductInformation(productVo))
					.operationStatus(true).build());
	}
	/**
	 * API invoked when we sell a product
	 * @param productName
	 * @return
	 */
	@DeleteMapping(value="/{productName}")
	public Mono<ProductServiceVO> sellProduct(@PathVariable("productName") String productName) {
		 return productService.sellProduct(productName);
	}

}
