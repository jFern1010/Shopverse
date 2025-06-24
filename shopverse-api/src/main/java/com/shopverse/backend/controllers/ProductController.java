package com.shopverse.backend.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shopverse.backend.dto.ProductDTO;
import com.shopverse.backend.dto.ProductRequestDTO;
import com.shopverse.backend.models.Category;
import com.shopverse.backend.models.Product;
import com.shopverse.backend.repositories.CategoryRepository;
import com.shopverse.backend.repositories.ProductRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/shopverse/products")
@Tag(name = "Products", description = "Endpoints for managing and retrieving product information")
public class ProductController {

	private final ProductRepository productRepo;

	private final CategoryRepository categoryRepo;

	public ProductController(ProductRepository productRepo, CategoryRepository categoryRepo) {
		this.productRepo = productRepo;
		this.categoryRepo = categoryRepo;
	}

	@GetMapping
	@Operation(summary = "Get all products", description = "Returns a list of all products")
	public List<ProductDTO> allProducts() {
		return productRepo.findAll().stream().map(ProductDTO::new).collect(Collectors.toList());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	@Operation(summary = "Create product", description = "Adds a new product to the catalog")
	public ResponseEntity<Object> createProduct(@RequestBody ProductRequestDTO dto) {
		Category category = categoryRepo.findById(dto.categoryId)
				.orElseThrow();

		Product product = new Product();
		product.setTitle(dto.title);
		product.setDescription(dto.description);
		product.setPrice(dto.price);
		product.setImageUrl(dto.imageUrl);
		product.setCategory(category);
		Product newProduct = productRepo.save(product);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newProduct.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/{productId}")
	@Operation(summary = "Get product by ID", description = "Returns details of a prodcut by its ID")
	public ResponseEntity<ProductDTO> getProduct(@PathVariable long productId) throws Exception {
		Product product = productRepo.findById(productId).orElseThrow(() -> new Exception("Product not found"));
		return ResponseEntity.ok(new ProductDTO(product));

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{productId}")
	@Operation(summary = "Update product", description = "Updates product details by ID")
	public ResponseEntity<?> updateProduct(@PathVariable long productId, @RequestBody ProductRequestDTO dto)
			throws Exception {
		Product productToUpdate = productRepo.findById(productId).orElseThrow(() -> new Exception("Product not found"));

		Category category = categoryRepo.findById(dto.getCategoryId())
				.orElseThrow(() -> new Exception("Category not found"));

		productToUpdate.setTitle(dto.getTitle());
		productToUpdate.setDescription(dto.getDescription());
		productToUpdate.setPrice(dto.getPrice());
		productToUpdate.setStock(10);
		productToUpdate.setCategory(category);
		productRepo.save(productToUpdate);

		return ResponseEntity.ok(new ProductDTO(productToUpdate));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{productId}")
	@Operation(summary = "Delete product", description = "Deletes a prodcut by ID")
	public ResponseEntity<Object> deleteByProduct(@PathVariable long productId) throws Exception {
		Product product = productRepo.findById(productId).orElseThrow(() -> new Exception("Product not found."));

		productRepo.delete(product);
		return ResponseEntity.noContent().build();

	}

	@GetMapping("/category/{category}")
	@Operation(summary = "Get products by category", description = "Returns products that belong to the specified category")
	public List<ProductDTO> allProductsByCategory(@PathVariable String category) {
		return productRepo.findAll().stream().filter(product -> product.getCategory().getName().equals(category))
				.map(ProductDTO::new)
				.collect(Collectors.toList());
	}

}
