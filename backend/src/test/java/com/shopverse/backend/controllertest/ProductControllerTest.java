package com.shopverse.backend.controllertest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopverse.backend.configuration.SecurityTestConfiguration;
import com.shopverse.backend.controllers.ProductController;
import com.shopverse.backend.dto.ProductRequestDTO;
import com.shopverse.backend.models.Category;
import com.shopverse.backend.models.Product;
import com.shopverse.backend.repositories.CategoryRepository;
import com.shopverse.backend.repositories.ProductRepository;

@WebMvcTest(ProductController.class)
@Import(SecurityTestConfiguration.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductRepository productRepo;

	@MockBean
	private CategoryRepository categoryRepo;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void allProducts_shouldReturnList() throws Exception {
		Product product = new Product();
		product.setId(1L);
		product.setTitle("Test Product");
		product.setPrice(10.0);
		Category cat = new Category("Electronics");
		product.setCategory(cat);

		when(productRepo.findAll()).thenReturn(List.of(product));

		mockMvc.perform(get("/shopverse/products")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Test Product")).andExpect(jsonPath("$[0].price").value(10.0));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createProduct_shouldSaveAndReturnCreated() throws Exception {
		ProductRequestDTO dto = new ProductRequestDTO("New Product", "Desc", 20.0, "http://img", 5, 1L);

		Category category = new Category("Electronics");
		category.setId(1L);

		Product saved = new Product();
		saved.setId(100L);
		saved.setTitle("New Product");
		saved.setCategory(category);

		when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
		when(productRepo.save(any(Product.class))).thenReturn(saved);

		mockMvc.perform(post("/shopverse/products").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isCreated())
				.andExpect(header().exists("Location"));
	}

	@Test
	void getProduct_shouldReturnProduct() throws Exception {
		Product product = new Product();
		product.setId(1L);
		product.setTitle("Test Product");
		product.setPrice(10.0);
		product.setCategory(new Category("Electronics"));

		when(productRepo.findById(1L)).thenReturn(Optional.of(product));

		mockMvc.perform(get("/shopverse/products/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Test Product")).andExpect(jsonPath("$.price").value(10.0));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void updateProduct_shouldUpdateAndReturnProduct() throws Exception {
		Product existing = new Product();
		existing.setId(1L);
		existing.setTitle("Old");
		existing.setCategory(new Category("Electronics"));

		ProductRequestDTO dto = new ProductRequestDTO("Updated", "Updated desc", 30.0, "http://img", 10, 1L);

		Category category = new Category("Electronics");
		category.setId(1L);

		when(productRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
		when(productRepo.save(any(Product.class))).thenReturn(existing);

		mockMvc.perform(put("/shopverse/products/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Updated")).andExpect(jsonPath("$.price").value(30.0));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteProduct_shouldDeleteAndReturnNoContent() throws Exception {
		Product product = new Product();
		product.setId(1L);
		product.setTitle("To Delete");
		product.setCategory(new Category("Electronics"));

		when(productRepo.findById(1L)).thenReturn(Optional.of(product));
		doNothing().when(productRepo).delete(product);

		mockMvc.perform(delete("/shopverse/products/1")).andExpect(status().isNoContent());

		verify(productRepo, times(1)).delete(product);
	}

	@Test
	void allProductsByCategory_shouldReturnFilteredList() throws Exception {
		Category cat = new Category("Electronics");
		Product product = new Product();
		product.setId(1L);
		product.setTitle("Phone");
		product.setCategory(cat);

		when(productRepo.findAll()).thenReturn(List.of(product));

		mockMvc.perform(get("/shopverse/products/category/Electronics")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Phone"));
	}

	@Test
	void searchProducts_shouldReturnResults() throws Exception {
		Product product = new Product();
		product.setId(1L);
		product.setTitle("Laptop");
		product.setDescription("Gaming laptop");
		product.setCategory(new Category("Electronics"));

		when(productRepo.searchByTitleDescription("Laptop")).thenReturn(List.of(product));

		mockMvc.perform(get("/shopverse/products/search?q=Laptop")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Laptop"));
	}

	// Negative-path tests with 404 expectations

	@Test
	void getProduct_shouldReturnNotFound_whenMissing() throws Exception {
		when(productRepo.findById(99L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/shopverse/products/99")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createProduct_shouldReturnNotFound_whenCategoryMissing() throws Exception {
		ProductRequestDTO dto = new ProductRequestDTO("New Product", "Desc", 20.0, "http://img", 5, 999L);

		when(categoryRepo.findById(999L)).thenReturn(Optional.empty());

		mockMvc.perform(post("/shopverse/products").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void updateProduct_shouldReturnNotFound_whenProductMissing() throws Exception {
		ProductRequestDTO dto = new ProductRequestDTO("Updated", "Updated desc", 30.0, "http://img", 10, 1L);

		when(productRepo.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(put("/shopverse/products/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void updateProduct_shouldReturnNotFound_whenCategoryMissing() throws Exception {
		Product existing = new Product();
		existing.setId(1L);
		existing.setTitle("Old");

		ProductRequestDTO dto = new ProductRequestDTO("Updated", "Updated desc", 30.0, "http://img", 10, 999L);

		when(productRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(categoryRepo.findById(999L)).thenReturn(Optional.empty());

		mockMvc.perform(put("/shopverse/products/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteProduct_shouldReturnNotFound_whenProductMissing() throws Exception {
		when(productRepo.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(delete("/shopverse/products/1")).andExpect(status().isNotFound());
	}
}