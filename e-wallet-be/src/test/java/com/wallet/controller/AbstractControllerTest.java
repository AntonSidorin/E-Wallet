package com.wallet.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.controller.auth.RegisterRequest;
import com.wallet.dto.WalletDto;
import com.wallet.service.auth.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    protected String getTokenViaRegistry(String username) throws Exception {
        RegisterRequest request = new RegisterRequest(null, null, username, "password");
        AuthenticationResponse authenticationResponse = mapper.readValue(
                mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                                .content(new ObjectMapper().writeValueAsBytes(request))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), AuthenticationResponse.class);

        return "Bearer " + authenticationResponse.getToken();
    }

    protected WalletDto createWallet(String token, WalletDto wallet) throws Exception {
        return mapper.readValue(
                mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                                .header(HttpHeaders.AUTHORIZATION, token)
                                .content(mapper.writeValueAsBytes(wallet))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(), WalletDto.class);
    }
}
