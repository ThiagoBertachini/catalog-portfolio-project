package com.portfolioproject.catalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolioproject.catalog.dto.CategoryDTO;
import com.portfolioproject.catalog.entities.Category;
import com.portfolioproject.catalog.repositories.CategoryRepository;
import com.portfolioproject.catalog.services.exceptions.ObjtNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		/*Para cada objeto category, um novo é instanciado como categoryDTO e retorna uma lista*/
		return categoryRepository.findAll().stream().map(
				cat -> new CategoryDTO(cat)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id){
		Optional<Category> objOpt = categoryRepository.findById(id);
		Category entity = objOpt.orElseThrow(
				() -> new ObjtNotFoundException("Entity not found for id: -[" + id + "]-"));
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO categoryDTO) {
		Category entity = new Category(categoryDTO);
		return new CategoryDTO(categoryRepository.save(entity));
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
		try {
			Category entity = categoryRepository.getOne(id);
			entity.setName(categoryDTO.getName());
			return new CategoryDTO(categoryRepository.save(entity));	
		}catch(EntityNotFoundException e) {
			throw new ObjtNotFoundException("Entity for update not found [" + id + "]");
		}
	}
}
