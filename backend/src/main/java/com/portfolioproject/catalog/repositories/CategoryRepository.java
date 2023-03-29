package com.portfolioproject.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolioproject.catalog.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
}
