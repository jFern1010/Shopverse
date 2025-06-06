package com.shopverse.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopverse.backend.models.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
