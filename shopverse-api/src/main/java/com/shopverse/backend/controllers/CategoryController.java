package com.shopverse.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopverse.backend.models.Category;
import com.shopverse.backend.repositories.CategoryRepository;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/shopverse/categories")
public class CategoryController {

	private final CategoryRepository categoryRepo;

	public CategoryController(CategoryRepository categoryRepo) {

		this.categoryRepo = categoryRepo;
	}

	@GetMapping
	@Operation(summary = "Get all categories", description = "Return a list of all categories")
	public List<Category> allCategories() {
		return categoryRepo.findAll();
	}

	@GetMapping("/{categoryId}")
	@Operation(summary = "Get category by ID", description = "Returns detail of a category by its ID")
	public ResponseEntity<Category> getCategory(@PathVariable Long categoryId) {
		return categoryRepo.findById(categoryId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	@Operation(summary = "Create category", description = "Adds a new category")
	public ResponseEntity<Category> createCategory(@RequestBody Category category) {
		Category savedCategory = categoryRepo.save(category);
		return ResponseEntity.ok(savedCategory);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{categoryId}")
	@Operation(summary = "Update category", description = "Updates an existing category")
	public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
		return categoryRepo.findById(categoryId).map(x -> {
			x.setName(category.getName());
			return ResponseEntity.ok(categoryRepo.save(x));
		}).orElse(ResponseEntity.notFound().build());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{categoryId}")
	@Operation(summary = "Dalete category", description = "Deletes a category")
	public ResponseEntity<Object> deleteCategory(@PathVariable Long categoryId) {
		return categoryRepo.findById(categoryId).map(x -> {
			categoryRepo.delete(x);
			return ResponseEntity.noContent().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}