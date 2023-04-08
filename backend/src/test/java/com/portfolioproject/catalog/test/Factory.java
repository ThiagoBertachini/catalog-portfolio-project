package com.portfolioproject.catalog.test;

import java.time.Instant;

import com.portfolioproject.catalog.dto.ProductDTO;
import com.portfolioproject.catalog.entities.Category;
import com.portfolioproject.catalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product productCategoryFactory = new Product(1L, "The Lord of the Rings", "Book, movie and game", 90.5, 
				"https://test.test.test", Instant.parse("2020-07-13T20:50:07.12345Z"));
		productCategoryFactory.getCategories().add(new Category(1L, "Electronics"));
		return productCategoryFactory;
	}
	

	public static ProductDTO createProductDTO() {
		Product productCategoryDTOFactory = createProduct();
		return new ProductDTO(productCategoryDTOFactory, productCategoryDTOFactory.getCategories());
	}
}
