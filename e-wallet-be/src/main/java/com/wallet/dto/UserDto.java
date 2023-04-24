package com.wallet.dto;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;

@Tag(name = "User")
public record UserDto(@Size(max=50) String firstname, @Size(max=50) String lastname) {}
