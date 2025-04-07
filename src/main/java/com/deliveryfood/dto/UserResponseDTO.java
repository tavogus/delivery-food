package com.deliveryfood.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(
    Long id,
    String name,
    String email,
    String phone,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {} 