package com.portfolioproject.catalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.portfolioproject.catalog.entities.Product;
import com.portfolioproject.catalog.test.Factory;

@DataJpaTest
public class ProductRepositoryTest{
	
	@Autowired
	private ProductRepository productRepository;
	
	private long validId = 1L;
	private long invalidId = 0L;
	private long countTotalProduct = 0L;
	
	@BeforeEach
	void setUp() throws Exception {
		validId = 1L;
		invalidId = 0L;
		countTotalProduct = 25L;
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
				 
		Product product = Factory.createProduct();
		product.setId(null);
		
		Product result = productRepository.save(product);
		
		Assertions.assertNotNull(result.getId());
		Assertions.assertEquals(countTotalProduct + 1, result.getId());
	}
	
	@Test
	public void deleteShouldObjectWhenIdExists() {
				 
		productRepository.deleteById(validId);
		Optional<Product> result = productRepository.findById(validId);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenInvalidId() {
		
		long invalidId = 0L;
		 
		Assertions.assertThrows(EmptyResultDataAccessException.class, 
				() -> {
					productRepository.deleteById(invalidId);
				});
	}
}
