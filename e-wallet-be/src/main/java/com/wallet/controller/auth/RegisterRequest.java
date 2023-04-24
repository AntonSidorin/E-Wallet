package com.wallet.controller.auth;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Size(min = 1, max = 50)
    private String firstname;
    @Size(min = 1, max = 50)
    private String lastname;
    @Size(min = 1, max = 50)
    private String username;
    @Size(min = 1, max = 50)
    private String password;
}
