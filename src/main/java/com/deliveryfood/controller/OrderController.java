package com.deliveryfood.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliveryfood.dto.OrderRatingRequestDTO;
import com.deliveryfood.dto.OrderRatingResponseDTO;
import com.deliveryfood.dto.OrderRequestDTO;
import com.deliveryfood.dto.OrderResponseDTO;
import com.deliveryfood.entity.OrderStatus;
import com.deliveryfood.service.OrderRatingService;
import com.deliveryfood.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRatingService orderRatingService;

    public OrderController(OrderService orderService, OrderRatingService orderRatingService) {
        this.orderService = orderService;
        this.orderRatingService = orderRatingService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(orderService.getOrdersByRestaurant(restaurantId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
    
    @PostMapping("/pay/{orderId}/user/{userId}")
    public ResponseEntity<OrderResponseDTO> payOrder(
            @PathVariable Long orderId,
            @PathVariable Long userId) {
        return ResponseEntity.ok(orderService.payOrder(orderId, userId));
    }

    @PostMapping("/{orderId}/rating")
    public ResponseEntity<OrderRatingResponseDTO> rateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderRatingRequestDTO request) {
        OrderRatingResponseDTO rating = orderRatingService.rateOrder(orderId, request);
        return ResponseEntity.ok(rating);
    }

    @GetMapping("/{orderId}/rating")
    public ResponseEntity<OrderRatingResponseDTO> getOrderRating(@PathVariable Long orderId) {
        OrderRatingResponseDTO rating = orderRatingService.getOrderRating(orderId);
        return ResponseEntity.ok(rating);
    }

    @GetMapping("/restaurant/{restaurantId}/ratings")
    public ResponseEntity<List<OrderRatingResponseDTO>> getRestaurantRatings(@PathVariable Long restaurantId) {
        List<OrderRatingResponseDTO> ratings = orderRatingService.getRestaurantRatings(restaurantId);
        return ResponseEntity.ok(ratings);
    }
} 