package com.shopverse.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopverse.backend.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
