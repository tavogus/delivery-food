package com.deliveryfood.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliveryfood.entity.Order;
import com.deliveryfood.entity.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByRestaurantId(Long restaurantId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
    List<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status);
} 