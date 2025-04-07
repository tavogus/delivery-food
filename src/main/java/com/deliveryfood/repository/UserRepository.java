package com.deliveryfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliveryfood.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
} 