package com.shopverse.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopverse.backend.models.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
