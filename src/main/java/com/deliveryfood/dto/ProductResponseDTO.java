package com.deliveryfood.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDTO(
    Long id,
    Long restaurantId,
    String name,
    String description,
    BigDecimal price,
    String category,
    String imageUrl,
    boolean available,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {} 