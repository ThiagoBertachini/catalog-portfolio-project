package com.portfolioproject.catalog.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.portfolioproject.catalog.dto.ProductDTO;
import com.portfolioproject.catalog.entities.Category;
import com.portfolioproject.catalog.entities.Product;
import com.portfolioproject.catalog.repositories.CategoryRepository;
import com.portfolioproject.catalog.repositories.ProductRepository;
import com.portfolioproject.catalog.services.exceptions.IntegrityDataBaseException;
import com.portfolioproject.catalog.services.exceptions.ObjtNotFoundException;
import com.portfolioproject.catalog.test.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
	
	private long validId;
	private long invalidId;
	private long releatedId;
	private PageImpl<Product> productPaged;
	private Product product;
	private ProductDTO productDto;

	@BeforeEach
	void setUp() throws Exception {
		
		validId = 1L;
		invalidId = 0L;
		releatedId = 2L;
		
		product = Factory.createProduct();
		productDto = Factory.createProductDTO();
		productPaged = new PageImpl<>(List.of(product));
		
		Mockito.doNothing().when(productRepository).deleteById(validId);	
		Mockito.doThrow(ObjtNotFoundException.class).when(productRepository)
		.deleteById(invalidId);	
		Mockito.doThrow(IntegrityDataBaseException.class).when(productRepository)
		.deleteById(releatedId);	
		Mockito.when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(productPaged);
		Mockito.when(productRepository.save(any())).thenReturn(product);
		Mockito.when(productRepository.findById(validId)).thenReturn(Optional.of(product));
		Mockito.when(productRepository.findById(invalidId)).thenReturn(Optional.empty());
		
		Mockito.when(categoryRepository.getOne(anyLong())).thenReturn(new Category());		
		}
	
	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryRepository categoryRepository;
	
	
	@Test
	public void updateShoudUpdateProductWhenValidId() {
		
		Product productEntity = Factory.createProduct();
				
		Mockito.when(productRepository.getOne(anyLong())).thenReturn(productEntity);
		Mockito.when(productRepository.save(any())).thenReturn(productEntity);

		Mockito.when(categoryRepository.getOne(anyLong())).thenReturn(new Category());

		ProductDTO result = productService.update(validId, productDto);
		
		Assertions.assertNotNull(result);
		Mockito.verify(productRepository, times(1)).save(productEntity);
	}
	
	@Test
	public void findByIdShoudThrowObjtNotFoundExceptionWhenInvalidId() {
				
		Assertions.assertThrows(ObjtNotFoundException.class, 
				() -> productService.findById(invalidId));
		
		Mockito.verify(productRepository, times(1)).findById(invalidId);
	}
	
	@Test
	public void findByIdShoudReturnProductWhenValidId() {
		
		ProductDTO result = productService.findById(validId);
		
		Assertions.assertNotNull(result);
		Mockito.verify(productRepository, times(1)).findById(validId);
	}
	
	@Test
	public void insertShoudSaveAndReturnProductWhenValidProduct() {
		
		ProductDTO result = productService.insert(productDto);
		
		Assertions.assertNotNull(result);
		Mockito.verify(productRepository, times(1)).save(any());
	}
	
	@Test
	public void findAllPagedShoudReturnProductPaged() {
		
		Page<ProductDTO> result = productService.findAllPaged(ArgumentMatchers.any());
		
		Assertions.assertNotNull(result.get());
		Mockito.verify(productRepository, times(1)).findAll((Pageable)ArgumentMatchers.any());
	}
	
	@Test
	public void deleteShoudDoNothingWhenValidId() {
				
		Assertions.assertDoesNotThrow(() -> {
			productService.delete(validId);
		});
		
		Mockito.verify(productRepository, times(1)).deleteById(validId);
	}
	
	@Test
	public void deleteShoudThrowEmptyResultDataAccessExceptionWhenInvalidId() {
				
		Assertions.assertThrows(ObjtNotFoundException.class, () -> {
			productService.delete(invalidId);
		});
		
		Mockito.verify(productRepository, times(1)).deleteById(invalidId);
	}
	
	@Test
	public void deleteShoudThrowIntegrityDataBaseExceptionWhenReleatedId() {
				
		Assertions.assertThrows(IntegrityDataBaseException.class, () -> {
			productService.delete(releatedId);
		});
		
		Mockito.verify(productRepository, times(1)).deleteById(releatedId);
	}
	
	
	
}
