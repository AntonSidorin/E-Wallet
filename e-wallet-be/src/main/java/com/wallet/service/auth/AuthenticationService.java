package com.wallet.service.auth;

import com.wallet.controller.auth.AuthenticationRequest;
import com.wallet.controller.auth.RegisterRequest;
import com.wallet.dao.entity.Role;
import com.wallet.dao.entity.User;
import com.wallet.dao.repository.UserRepository;
import com.wallet.exception.UserFoundException;
import com.wallet.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        repository.findByUsername(request.getUsername())
                .ifPresent(user -> {
                    throw new UserFoundException(user.getUsername());
                });

        var user = repository.save(
                User.builder()
                        .firstname(request.getFirstname())
                        .lastname(request.getLastname())
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.USER)
                        .build());

        return createResponse(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        //User is authenticated
        var user = repository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUsername()));

        return createResponse(user);
    }

    private AuthenticationResponse createResponse(User user){

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("firstname", user.getFirstname());
        extraClaims.put("lastname", user.getLastname());

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(extraClaims, user))
                .build();
    }

}
