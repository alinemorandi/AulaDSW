package com.example.AulaDSW.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.AulaDSW.entities.Category;
import com.example.AulaDSW.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Transactional(readOnly = true)
	@Query("SELECT obj FROM Product obj INNER JOIN obj.categories cats WHERE :category IN cats")
	Page<Product> findByCategory(Category category, Pageable pageable);

	
}
