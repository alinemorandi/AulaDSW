package com.example.AulaDSW.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.AulaDSW.dto.CategoryDTO;
import com.example.AulaDSW.dto.ProductCategoriesDTO;
import com.example.AulaDSW.dto.ProductDTO;
import com.example.AulaDSW.entities.Category;
import com.example.AulaDSW.entities.Product;
import com.example.AulaDSW.repositories.CategoryRepository;
import com.example.AulaDSW.repositories.ProductRepository;
import com.example.AulaDSW.services.exceptions.DatabaseException;
import com.example.AulaDSW.services.exceptions.ParamFormatException;
import com.example.AulaDSW.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public Page<ProductDTO> findByNameCategoryPaged(String name, String categoriesStr, Pageable pageable) {
		Page<Product> list;
		if (categoriesStr.equals("")) {
			list = productRepository.findByNameContainingIgnoreCase(name, pageable);
		} else {
			List<Long> ids = parseIds(categoriesStr);
			List<Category> categories = ids.stream().map(id -> categoryRepository.getOne(id)).collect(Collectors.toList());
			list = productRepository.findByNameContainingIgnoreCaseAndCategoriesIn(name, categories, pageable);
		}
		return list.map(e -> new ProductDTO(e));
		}
	
	private List<Long> parseIds(String categoriesStr) {
		String[] idsArray = categoriesStr.split(",");
		List<Long> list = new ArrayList<>();
		for(String idStr : idsArray) {
			try {
				list.add(Long.parseLong(idStr));
			} catch(NumberFormatException e) {
				throw new ParamFormatException("Invalid categories format");
			}
		}
		return list;
	}
	
	public ProductDTO findById(Long id) {
		Optional<Product> obj = productRepository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new ProductDTO(entity);
	}
	
	@Transactional
	public ProductDTO insert(ProductCategoriesDTO dto){
		Product entity = dto.toEntity();
		setProductCategories(entity, dto.getCategories());
		entity = productRepository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductCategoriesDTO dto){
		try{
			Product entity = productRepository.getOne(id); //Instancio um usuario baseado no id usando getOne
			updateData(entity, dto); //atualizo os dados do usuario com base nos dto enviados na requisição
			entity = productRepository.save(entity); //salvo no banco
			return new ProductDTO(entity); //converto
		}catch(EntityNotFoundException e){
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Product entity, ProductCategoriesDTO dto) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());

		if (dto.getCategories() != null && dto.getCategories().size() > 0){
			setProductCategories(entity, dto.getCategories());
		}
	}
	
	public void delete(Long id){
		try{
			productRepository.deleteById(id);
		}catch (EmptyResultDataAccessException e){
			throw new  ResourceNotFoundException(id);
		}catch (DataIntegrityViolationException e){
			throw new DatabaseException(e.getMessage());
		}
	}
	
	private void setProductCategories(Product entity, List<CategoryDTO> categories){
		entity.getCategories().clear();
		for (CategoryDTO dto : categories){
			Category category = categoryRepository.getOne(dto.getId());
			entity.getCategories().add(category);
		}
	}
	
	@Transactional
	public Page<ProductDTO> findByCategoryPaged(Long categoryId, Pageable pageable) {
		Category category = categoryRepository.getOne(categoryId);
		Page<Product> products = productRepository.findByCategory(category, pageable);
		return products.map(e -> new ProductDTO(e));
	}
	
	@Transactional
	public void addCategory(Long id, CategoryDTO dto) {
		Product product = productRepository.getOne(id);
		Category category = categoryRepository.getOne(dto.getId());
		product.getCategories().add(category);
		productRepository.save(product);
	}

	
	@Transactional
	public void removeCategory(Long id, CategoryDTO dto) {
		Product product = productRepository.getOne(id);
		Category category = categoryRepository.getOne(dto.getId());
		product.getCategories().remove(category);
		productRepository.save(product);
	}

	@Transactional
	public void setCategories(Long id, List<CategoryDTO> dto) {
		Product product = productRepository.getOne(id);
		setProductCategories(product, dto);
		productRepository.save(product);
	}

	
	

}
