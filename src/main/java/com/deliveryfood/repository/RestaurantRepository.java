package com.deliveryfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliveryfood.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    boolean existsByName(String name);
} 