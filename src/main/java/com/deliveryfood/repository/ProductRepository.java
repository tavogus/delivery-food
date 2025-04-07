package com.deliveryfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliveryfood.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByRestaurantId(Long restaurantId);
    List<Product> findByRestaurantIdAndAvailable(Long restaurantId, boolean available);
    List<Product> findByCategory(String category);
} 