package com.ikea.productservice;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ikea.productservice.entities.Article;
import com.ikea.productservice.entities.Product;
import com.ikea.productservice.repository.ProductRepository;

//@SpringBootTest()
@ExtendWith(SpringExtension.class)
@DataJpaTest
@PropertySource("classpath:product-service-test.properties")
public class ProductRepositoryApplicationTests {
	
	
	@Autowired
	private ProductRepository prodRepo;
	
	@Test
	@DisplayName("save product test")
	void saveProductTest() {
		Product product=new Product();
		product.setName("testProduct");
		Article article=new Article();
		article.setAmountOf("5");
		article.setArtId("1");
		article.setProduct(product);
		product.setArticles(List.of(article));
		prodRepo.save(product);
		Optional<Product> retrieved=prodRepo.findByName("testProduct");
		assertAll(()->retrieved.isPresent(),()->assertEquals("testProduct", retrieved.get().getName()));
	}

}
