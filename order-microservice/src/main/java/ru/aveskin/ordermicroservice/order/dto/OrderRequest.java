package ru.aveskin.ordermicroservice.order.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record OrderRequest(
        @NotBlank
        String userId,
        @NotBlank
        BigDecimal amount
) {
}
