package com.portfolioproject.catalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolioproject.catalog.dto.CategoryDTO;
import com.portfolioproject.catalog.repositories.CategoryRepository;

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
}
