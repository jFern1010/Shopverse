package com.shopverse.backend.servicetest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
	}

	@Test
	void populateProducts_shouldCreateAndSaveProduct_whenNoProductExist() {
		when(productRepo.count()).thenReturn(0L);

		ProductDTO dto = new ProductDTO();
		dto.title = "Test Title";
		dto.category = "Electronics";
		dto.description = "Test Description";
		dto.price = 49.99;
		dto.image = "http://test.image.url";

		when(restTemplate.getForObject(anyString(), eq(ProductDTO[].class))).thenReturn(new ProductDTO[] { dto });

		Category mockCategory = new Category("Electronics");

		when(categoryRepo.findByName("Electronics")).thenReturn(Optional.of(mockCategory));

		productSeedService.populateProducts();

		verify(productRepo, times(1)).save(any());
	}

}
