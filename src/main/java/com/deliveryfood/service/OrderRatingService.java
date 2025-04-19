package com.deliveryfood.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliveryfood.dto.OrderRatingRequestDTO;
import com.deliveryfood.dto.OrderRatingResponseDTO;
import com.deliveryfood.entity.Order;
import com.deliveryfood.entity.OrderRating;
import com.deliveryfood.entity.OrderStatus;
import com.deliveryfood.entity.Restaurant;
import com.deliveryfood.repository.OrderRatingRepository;
import com.deliveryfood.repository.OrderRepository;
import com.deliveryfood.repository.RestaurantRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderRatingService {
    private final OrderRatingRepository orderRatingRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;

    public OrderRatingService(
            OrderRatingRepository orderRatingRepository, 
            OrderRepository orderRepository,
            RestaurantRepository restaurantRepository) {
        this.orderRatingRepository = orderRatingRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public OrderRatingResponseDTO rateOrder(Long orderId, OrderRatingRequestDTO request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Only delivered orders can be rated");
        }

        if (orderRatingRepository.existsByOrder(order)) {
            throw new IllegalStateException("Order has already been rated");
        }

        OrderRating rating = new OrderRating();
        rating.setOrder(order);
        rating.setRating(request.rating());
        rating.setComment(request.comment());

        rating = orderRatingRepository.save(rating);
        return toResponse(rating);
    }

    public OrderRatingResponseDTO getOrderRating(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        OrderRating rating = orderRatingRepository.findByOrder(order)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));

        return toResponse(rating);
    }

    public List<OrderRatingResponseDTO> getRestaurantRatings(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        List<OrderRating> ratings = orderRatingRepository.findByOrderRestaurant(restaurant);
        return ratings.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private OrderRatingResponseDTO toResponse(OrderRating rating) {
        return new OrderRatingResponseDTO(
            rating.getId(),
            rating.getOrder().getId(),
            rating.getRating(),
            rating.getComment(),
            rating.getCreatedAt(),
            rating.getUpdatedAt()
        );
    }
} 