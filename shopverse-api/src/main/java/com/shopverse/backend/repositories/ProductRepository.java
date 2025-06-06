package com.shopverse.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopverse.backend.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
