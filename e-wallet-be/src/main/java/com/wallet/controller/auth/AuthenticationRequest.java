package com.wallet.controller.auth;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Size(min = 1, max = 50)
    private String username;
    @Size(min = 1, max = 50)
    private String password;
}
