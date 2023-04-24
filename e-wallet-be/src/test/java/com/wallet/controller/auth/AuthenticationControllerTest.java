package com.wallet.controller.auth;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register() throws Exception {

        String username = "username";
        String password = "password";

        RegisterRequest request = new RegisterRequest(null, null, username, password);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticate() throws Exception {

        String username = "username_auth";
        String password = "password";

        RegisterRequest registerRequest = new RegisterRequest(null, null, username, password);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .content(new ObjectMapper().writeValueAsBytes(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/authenticate")
                        .content(new ObjectMapper().writeValueAsBytes(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}