package com.example.AulaDSW.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.AulaDSW.dto.CategoryDTO;
import com.example.AulaDSW.dto.CategoryInsertDTO;
import com.example.AulaDSW.entities.Category;
import com.example.AulaDSW.entities.Product;
import com.example.AulaDSW.repositories.CategoryRepository;
import com.example.AulaDSW.repositories.ProductRepository;
import com.example.AulaDSW.services.exceptions.DatabaseException;
import com.example.AulaDSW.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;
	
	public List<CategoryDTO> findAll() {
		
		List<Category> list = categoryRepository.findAll();
		return list.stream().map(e -> new CategoryDTO(e)).collect(Collectors.toList());
	}
	
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = categoryRepository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));

		return new CategoryDTO(entity);
	}

	public CategoryDTO insert(CategoryInsertDTO dto){
		Category entity = dto.toEntity();
		entity = categoryRepository.save(entity);
		return new CategoryDTO(entity);
	}

	public void delete(Long id){
		try{
			categoryRepository.deleteById(id);
		}catch (EmptyResultDataAccessException e){
			throw new ResourceNotFoundException(id);
		}catch(DataIntegrityViolationException e){
			throw new DatabaseException(e.getMessage());
		}
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto){
		try{
			Category entity = categoryRepository.getOne(id);
			updateData(entity, dto);
			entity = categoryRepository.save(entity);
			return new CategoryDTO(entity);

		}catch (EntityNotFoundException e){
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Category entity, CategoryDTO dto){
		entity.setName(dto.getName());
	}
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findByProduct(Long productId) {
		Product product = productRepository.getOne(productId);
		Set<Category> set = product.getCategories();
		return set.stream().map(e -> new CategoryDTO(e)).collect(Collectors.toList());
	}

}
