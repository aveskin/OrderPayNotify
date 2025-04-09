package ru.aveskin.app.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record OrderRequest(
        @NotBlank
        String userId,
        BigDecimal amount
) {
}
