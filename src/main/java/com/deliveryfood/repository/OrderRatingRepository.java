package com.deliveryfood.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliveryfood.entity.Order;
import com.deliveryfood.entity.OrderRating;
import com.deliveryfood.entity.Restaurant;

@Repository
public interface OrderRatingRepository extends JpaRepository<OrderRating, Long> {
    Optional<OrderRating> findByOrder(Order order);
    boolean existsByOrder(Order order);
    List<OrderRating> findByOrderRestaurant(Restaurant restaurant);
} 