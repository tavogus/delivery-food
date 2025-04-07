package com.deliveryfood.dto;

public record OrderItemRequestDTO(
    Long productId,
    Integer quantity,
    String notes
) {} 