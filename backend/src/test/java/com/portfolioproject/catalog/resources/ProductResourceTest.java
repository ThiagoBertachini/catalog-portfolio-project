package com.portfolioproject.catalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolioproject.catalog.dto.ProductDTO;
import com.portfolioproject.catalog.entities.Product;
import com.portfolioproject.catalog.services.ProductService;
import com.portfolioproject.catalog.services.exceptions.IntegrityDataBaseException;
import com.portfolioproject.catalog.services.exceptions.ObjtNotFoundException;
import com.portfolioproject.catalog.test.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTest{
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	/*Mockar componente sem integracao, carrega contexto do spring 
	e substitui o componente especifico por um mockado*/
	@MockBean 	
	private ProductService productService;
	
	private long validId;
	private long invalidId;
	private long relatedId;
	
	private Product product;
	private ProductDTO productDto;
	private PageImpl<Product> productPaged;
	private PageImpl<ProductDTO> productDTOPaged;

	@BeforeEach
	void setUp() throws Exception {
		
		validId = 1L;
		invalidId = 0L;
		relatedId = 1000L;
		
		product = Factory.createProduct();
		productPaged = new PageImpl<>(List.of(product));
		productDto = Factory.createProductDTO();
		productDTOPaged = new PageImpl<>(List.of(productDto));
		
		when(productService.findAllPaged(any())).thenReturn(productDTOPaged);
		
		when(productService.findById(validId)).thenReturn(productDto);
		Mockito.doThrow(ObjtNotFoundException.class).when(productService)
		.findById(invalidId);
		
		when(productService.update(eq(validId), any())).thenReturn( productDto);
		when(productService.update(eq(invalidId), any())).thenThrow(ObjtNotFoundException.class);
		
		doNothing().when(productService).delete(validId);
		doThrow(ObjtNotFoundException.class).when(productService).delete(invalidId);
		doThrow(IntegrityDataBaseException.class).when(productService).delete(relatedId);

	}
	
	@Test
	public void findAllPagedShouldReturnPage() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());	
	}
	
	@Test
	public void findByIdShouldReturnProductWhenValidId() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/products/" + validId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());	
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWheninvalidId() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/products/" + invalidId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldUpdateAndReturnProductWhenValidId() throws Exception {
		
		String objtJsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result = 
				mockMvc.perform(put("/products/" + validId)
						.content(objtJsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());	
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundWheninvalidId() throws Exception {
		
		String objtJsonBody = objectMapper.writeValueAsString(productDto);

		ResultActions result = 
				mockMvc.perform(put("/products/" + invalidId)
						.content(objtJsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenValidId() throws Exception {
				
		ResultActions result = 
				mockMvc.perform(delete("/products/" + validId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());	
	}

	
	@Test
	public void deleteShouldThrowObjtNotFoundExceptionWheninvalidId() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(delete("/products/" + invalidId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldThrowIntegrityDataBaseExceptionWhenRelatedId() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(delete("/products/" + relatedId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isBadRequest());
	}	
}
