package com.deliveryfood.dto;

import java.math.BigDecimal;

public record RestaurantRequestDTO(
    String name,
    String description,
    String address,
    String phone,
    String openingHours,
    BigDecimal deliveryFee,
    BigDecimal minimumOrder
) {} 