package com.portfolioproject.catalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.portfolioproject.catalog.dto.CategoryDTO;
import com.portfolioproject.catalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO> categoryList = categoryService.findAll();
		return ResponseEntity.ok().body(categoryList);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
		CategoryDTO categoryDTO = categoryService.findById(id);
		return ResponseEntity.ok().body(categoryDTO);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO categoryDTO){
		categoryDTO = categoryService.insert(categoryDTO);
		/*insere no header da resposta o endereco*/
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(categoryDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(categoryDTO);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> update(@PathVariable Long id, 
			@RequestBody CategoryDTO categoryDTO){
		categoryDTO = categoryService.update(id, categoryDTO);		
		return ResponseEntity.ok(categoryDTO);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		categoryService.delete(id);		
		return ResponseEntity.noContent().build();
	}

}
