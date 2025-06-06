package com.shopverse.backend.services;

import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shopverse.backend.dto.ProductDTO;
import com.shopverse.backend.models.Category;
import com.shopverse.backend.models.Product;
import com.shopverse.backend.repositories.CategoryRepository;
import com.shopverse.backend.repositories.ProductRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ProductSeedService {

	private final ProductRepository productRepo;
	private final CategoryRepository categoryRepo;

	public ProductSeedService(ProductRepository productRepo, CategoryRepository categoryRepo) {
		this.productRepo = productRepo;
		this.categoryRepo = categoryRepo;
	}

	@PostConstruct
	public void populateProducts() {
		if (productRepo.count() > 0) {
			return;
		}

		RestTemplate restTemplate = new RestTemplate();
		ProductDTO[] productDTOs = restTemplate.getForObject("https://fakestoreapi.com/products", ProductDTO[].class);
		
		if (productDTOs != null) {
			Arrays.stream(productDTOs).forEach(dto -> {
				Category category =  categoryRepo.findByName(dto.category)
						.orElseGet(() -> {
							Category newCategory = new Category(dto.category);
							return categoryRepo.save(newCategory);
						});
				
				Product product = new Product();
				product.setTitle(dto.title);
				product.setDescription(dto.description);
                product.setPrice(dto.price);
                product.setStock(10);
                product.setImageUrl(dto.image);
                product.setCategory(category);
                
                productRepo.save(product);

			});
		}

	}

}
