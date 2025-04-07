package com.deliveryfood.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderItemResponseDTO(
    Long id,
    Long productId,
    String productName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal,
    String notes,
    LocalDateTime createdAt
) {} 