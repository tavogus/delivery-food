package com.deliveryfood.dto;

import java.util.List;

public record OrderRequestDTO(
    Long userId,
    Long restaurantId,
    String deliveryAddress,
    String paymentMethod,
    List<OrderItemRequestDTO> items
) {} 