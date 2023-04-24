package com.wallet.service.auth;

import com.wallet.controller.auth.AuthenticationRequest;
import com.wallet.controller.auth.RegisterRequest;
import com.wallet.dao.entity.Role;
import com.wallet.dao.entity.User;
import com.wallet.dao.repository.UserRepository;
import com.wallet.exception.UserFoundException;
import com.wallet.security.JwtService;
import com.wallet.service.mapper.user.UserToUserDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserToUserDtoMapper userToUserDtoMapper;

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


        return AuthenticationResponse.builder()
                .user(userToUserDtoMapper.apply(user))
                .token(jwtService.generateToken(user))
                .build();
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

        return AuthenticationResponse.builder()
                .user(userToUserDtoMapper.apply(user))
                .token(jwtService.generateToken(user))
                .build();
    }

}
