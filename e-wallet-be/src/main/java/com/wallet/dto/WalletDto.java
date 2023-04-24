package com.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record WalletDto(
        String id,
        @NotBlank(message = "Name shouldn't be blank") @Size(max=50) String name,
        @Size(max = 250) String description,
        @DecimalMin(value = "0.0") BigDecimal balance) {
}
