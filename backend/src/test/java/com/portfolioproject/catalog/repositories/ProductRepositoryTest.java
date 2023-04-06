package com.portfolioproject.catalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.portfolioproject.catalog.entities.Product;

@DataJpaTest
public class ProductRepositoryTest{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Test
	public void deleteShouldObjectWhenIdExists() {
		
		long validId = 1L;
		 
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
