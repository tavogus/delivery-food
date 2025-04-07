package com.deliveryfood.dto;

import java.math.BigDecimal;

public record ProductRequestDTO(
    Long restaurantId,
    String name,
    String description,
    BigDecimal price,
    String category,
    String imageUrl,
    boolean available
) {} 