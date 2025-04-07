package com.deliveryfood.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliveryfood.entity.OrderStatus;

public record OrderResponseDTO(
    Long id,
    Long userId,
    String userName,
    Long restaurantId,
    String restaurantName,
    OrderStatus status,
    BigDecimal totalAmount,
    String deliveryAddress,
    String paymentMethod,
    Boolean paid,
    List<OrderItemResponseDTO> items,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {} 