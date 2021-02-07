package com.ikea.productservice;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.ikea.productservice.client.ProductServiceClient;
import com.ikea.productservice.entities.Article;
import com.ikea.productservice.entities.Product;
import com.ikea.productservice.exception.ProductServiceException;
import com.ikea.productservice.repository.ProductRepository;
import com.ikea.productservice.service.ProductService;
import com.ikea.productservice.vo.ArticleVO;
import com.ikea.productservice.vo.ProductServiceVO;
import com.ikea.productservice.vo.ProductVO;

import reactor.core.publisher.Mono;

@SpringBootTest(classes = { ProductServiceApplication.class })
@ExtendWith(SpringExtension.class)
@Transactional
@PropertySource("classpath:product-service-test.properties")
class ProductServiceApplicationTests {

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceApplicationTests.class);


	
	@Autowired
	private ProductService productService;

	@MockBean
	private ProductRepository productRepo;

	@Mock
	private ProductServiceClient productClient;

	@Value("${data.source.path}")
	String productSourcePath;

	@BeforeEach
	void setUp() {
		LOG.info("Test setup" + productSourcePath);
		Product product=new Product();
		product.setName("testProduct");
		Article article=new Article();
		article.setAmountOf("5");
		article.setArtId("1");
		article.setProduct(product);
		product.setArticles(List.of(article));
		ProductVO productVo = ProductVO.builder().name("testProduct")
				.articles(List.of(ArticleVO.builder().amountOf("2").artId("12").build())).build();
		productService.saveProductInformation(productVo);
	}

	@Test
	@DisplayName("Test for persisting product information")
	void testSaveProductInformation() {
		ProductVO productVo = ProductVO.builder().name("testProduct")
				.articles(List.of(ArticleVO.builder().amountOf("4").artId("14").build())).build();
		ProductVO savedVo = productService.saveProductInformation(productVo);
		LOG.info(productService.retrieveProductInformation().toString());
		assertEquals(productVo, savedVo);

	}

	@Test
	@DisplayName("Test for retrieving all product information")
	void testRetrieveProducInfo() {
		Product product=new Product();
		product.setName("testProduct");
		Article article=new Article();
		article.setAmountOf("5");
		article.setArtId("1");
		article.setProduct(product);
		product.setArticles(List.of(article));
		ProductServiceVO prodVo = productService.retrieveProductInformation();
		LOG.info("Product VO:{}", prodVo);
		when(productRepo.findAll()).thenReturn(List.of(product));
		assertAll(() -> assertNotNull(prodVo));
	}

	@Test
	@DisplayName("Test for selling a product exception")
	void testSellProducInfoException() {
		when(productClient.updateWarehouseInventory(Mockito.any(ProductServiceVO.class)))
				.thenReturn(Mono.just(ProductVO.builder().build()));
		Product product=new Product();
		product.setName("testProduct");
		Article article=new Article();
		article.setAmountOf("5");
		article.setArtId("1");
		article.setProduct(product);
		product.setArticles(List.of(article));
		when(productRepo.findByName(Mockito.anyString())).thenReturn(Optional.of(product));
		doNothing().when(productRepo).deleteByName(Mockito.anyString());


	}
	
	@Test
	@DisplayName("Test for selling a product")
	void testSellProducInfo() {
		when(productClient.updateWarehouseInventory(Mockito.any(ProductServiceVO.class)))
				.thenReturn(Mono.just(ProductVO.builder().build()));
		assertThrows(
				ProductServiceException.class,
		           () -> productService.sellProduct("testProductNotPresent"),
		           "Expected doThing() to throw, but it didn't"
		    );
	}
}
