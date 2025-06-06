package com.shopverse.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopverse.backend.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
