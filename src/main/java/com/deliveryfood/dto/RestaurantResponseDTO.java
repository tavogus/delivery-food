package com.deliveryfood.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RestaurantResponseDTO(
    Long id,
    String name,
    String description,
    String address,
    String phone,
    String openingHours,
    BigDecimal deliveryFee,
    BigDecimal minimumOrder,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {} 