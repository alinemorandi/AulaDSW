package com.example.AulaDSW.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.AulaDSW.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
