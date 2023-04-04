package com.portfolioproject.catalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolioproject.catalog.dto.ProductDTO;
import com.portfolioproject.catalog.entities.Category;
import com.portfolioproject.catalog.entities.Product;
import com.portfolioproject.catalog.repositories.CategoryRepository;
import com.portfolioproject.catalog.repositories.ProductRepository;
import com.portfolioproject.catalog.services.exceptions.IntegrityDataBaseException;
import com.portfolioproject.catalog.services.exceptions.ObjtNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
		/*Para cada objeto category, um novo é instanciado como categoryDTO e retorna uma lista*/
		return productRepository.findAll(pageRequest).map(
				cat -> new ProductDTO(cat));
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id){
		Optional<Product> objOpt = productRepository.findById(id);
		Product entity = objOpt.orElseThrow(
				() -> new ObjtNotFoundException("Entity not found for id: -[" + id + "]-"));
		return new ProductDTO(entity, entity.getCategories());
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO productDTO) {
		Product entity = new Product();
		entity = copyDtoToEntity(productDTO, entity);
		return new ProductDTO(productRepository.save(entity));
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		try {
			Product entity = productRepository.getOne(id);
			entity = copyDtoToEntity(productDTO, entity);
			return new ProductDTO(productRepository.save(entity));	
		}catch(EntityNotFoundException e) {
			throw new ObjtNotFoundException("Entity for update not found [" + id + "]");
		}
	}

	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new ObjtNotFoundException("Entity for delete not found [" + id + "]");
		}catch (DataIntegrityViolationException e) {
			throw new IntegrityDataBaseException("Integrity violation");
		}
	}
	
	private Product copyDtoToEntity(ProductDTO productDTO, Product entity) {
		entity.setName(productDTO.getName());
		entity.setPrice(productDTO.getPrice());
		entity.setDescription(productDTO.getDescription());
		entity.setDate(productDTO.getDate());
		entity.setImgUrl(productDTO.getImgUrl());
		
		entity.getCategories().clear();
		  
		productDTO.getCategories().forEach(catDto -> {
			Category catEntity = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(catEntity);
		});
		return entity;
	}
}
