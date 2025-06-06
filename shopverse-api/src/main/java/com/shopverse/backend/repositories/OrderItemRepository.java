package com.shopverse.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopverse.backend.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
