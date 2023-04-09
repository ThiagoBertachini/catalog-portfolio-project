package com.portfolioproject.catalog.services;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.portfolioproject.catalog.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
	
	private long validId = 1L;
	private long invalidId = 0L;
	
	@BeforeEach
	void setUp() throws Exception {
		validId = 1L;
		invalidId = 0L;
		
		Mockito.doNothing().when(productRepository).deleteById(validId);	
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository)
		.deleteById(invalidId);	

		}
	
	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;
	
	@Test
	public void deleteShoudDoNothingWhenValidId() {
				
		Assertions.assertDoesNotThrow(() -> {
			productService.delete(validId);
		});
		
		Mockito.verify(productRepository, times(1)).deleteById(validId);
	}
	
	
	
}
