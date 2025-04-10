package com.deliveryfood.dto;

import java.time.LocalDateTime;

public record OrderRatingResponseDTO(
    Long id,
    Long orderId,
    Integer rating,
    String comment,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {} 