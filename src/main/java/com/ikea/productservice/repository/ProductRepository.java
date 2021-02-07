package com.ikea.productservice.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ikea.productservice.entities.Product;


/**
 * Repository pattern for handling the product operations
 * 
 * @author Dharmvir Tiwari
 *
 */
@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

	public void removeByName(String name);
	public Optional<Product> findByName(String name);
	
	public void deleteByName(String name);
}
