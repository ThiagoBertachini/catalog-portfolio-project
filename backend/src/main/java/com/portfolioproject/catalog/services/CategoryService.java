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

import com.portfolioproject.catalog.dto.CategoryDTO;
import com.portfolioproject.catalog.entities.Category;
import com.portfolioproject.catalog.repositories.CategoryRepository;
import com.portfolioproject.catalog.services.exceptions.IntegrityDataBaseException;
import com.portfolioproject.catalog.services.exceptions.ObjtNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
		/*Para cada objeto category, um novo é instanciado como categoryDTO e retorna uma lista*/
		return categoryRepository.findAll(pageRequest).map(
				cat -> new CategoryDTO(cat));
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

	public void delete(Long id) {
		try {
		categoryRepository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new ObjtNotFoundException("Entity for delete not found [" + id + "]");
		}catch (DataIntegrityViolationException e) {
			throw new IntegrityDataBaseException("Integrity violation");
		}
	}
}
