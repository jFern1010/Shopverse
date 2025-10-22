package com.shopverse.backend.servicetest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.shopverse.backend.dto.ProductDTO;
import com.shopverse.backend.models.Category;
import com.shopverse.backend.models.Product;
import com.shopverse.backend.repositories.CategoryRepository;
import com.shopverse.backend.repositories.ProductRepository;
import com.shopverse.backend.services.ProductSeedService;

@ExtendWith(MockitoExtension.class)
class ProductSeedServiceTest {

	@Mock
	private ProductRepository productRepo;
	@Mock
	private CategoryRepository categoryRepo;
	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private ProductSeedService productSeedService;

	@Test
	void populateProducts_shouldDoNothing_ifProductsExist() {
		when(productRepo.count()).thenReturn(10L);

		productSeedService.populateProducts();

		verify(productRepo, never()).save(any());
		verify(restTemplate, never()).getForObject(anyString(), eq(ProductDTO[].class));
	}

	@Test
	void populateProducts_shouldCreateAndSaveProduct_whenNoProductExist_andCategoryExists() {
		when(productRepo.count()).thenReturn(0L);

		ProductDTO dto = new ProductDTO();
		dto.title = "Test Title";
		dto.category = "Electronics";
		dto.description = "Test Description";
		dto.price = 49.99;
		dto.imageUrl = "http://test.image.url";
		dto.setStock(5);

		when(restTemplate.getForObject(anyString(), eq(ProductDTO[].class))).thenReturn(new ProductDTO[] { dto });

		Category mockCategory = new Category("Electronics");
		when(categoryRepo.findByName("Electronics")).thenReturn(Optional.of(mockCategory));

		productSeedService.populateProducts();

		verify(productRepo, times(1)).save(any(Product.class));
		verify(categoryRepo, never()).save(any());
	}

	@Test
	void populateProducts_shouldCreateCategory_ifNotExists() {
		when(productRepo.count()).thenReturn(0L);

		ProductDTO dto = new ProductDTO();
		dto.title = "Another Product";
		dto.category = "Books";
		dto.description = "Book description";
		dto.price = 19.99;
		dto.imageUrl = "http://test.image.url";
		dto.setStock(3);

		when(restTemplate.getForObject(anyString(), eq(ProductDTO[].class))).thenReturn(new ProductDTO[] { dto });

		when(categoryRepo.findByName("Books")).thenReturn(Optional.empty());
		when(categoryRepo.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

		productSeedService.populateProducts();

		verify(categoryRepo, times(1)).save(any(Category.class));
		verify(productRepo, times(1)).save(any(Product.class));
	}

	@Test
	void populateProducts_shouldDoNothing_ifApiReturnsNull() {
		when(productRepo.count()).thenReturn(0L);
		when(restTemplate.getForObject(anyString(), eq(ProductDTO[].class))).thenReturn(null);

		productSeedService.populateProducts();

		verify(productRepo, never()).save(any());
	}

	@Test
	void populateProducts_shouldFallbackToDefaultStock_ifStockIsZero() {
		when(productRepo.count()).thenReturn(0L);

		ProductDTO dto = new ProductDTO();
		dto.title = "Zero Stock Product";
		dto.category = "Misc";
		dto.description = "Desc";
		dto.price = 9.99;
		dto.imageUrl = "http://test.image.url";
		dto.setStock(0);

		when(restTemplate.getForObject(anyString(), eq(ProductDTO[].class))).thenReturn(new ProductDTO[] { dto });

		Category mockCategory = new Category("Misc");
		when(categoryRepo.findByName("Misc")).thenReturn(Optional.of(mockCategory));

		productSeedService.populateProducts();

		verify(productRepo).save(argThat(product -> product.getStock() == 10));
	}
}