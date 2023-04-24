package com.wallet.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.wallet.controller.auth.AuthenticationRequest;
import com.wallet.controller.auth.RegisterRequest;
import com.wallet.dao.entity.Role;
import com.wallet.dao.entity.User;
import com.wallet.dao.repository.UserRepository;
import com.wallet.exception.UserFoundException;
import com.wallet.security.JwtService;
import com.wallet.service.mapper.user.UserToUserDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Spy
    private AuthenticationManager authenticationManager;

    @Spy
    private UserToUserDtoMapper userToUserDtoMapper = new UserToUserDtoMapper();

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void register() {

        //given
        String username = "username";
        String password = "password";
        String firstname = "firstname";
        String lastname = "lastname";

        RegisterRequest request = new RegisterRequest(
                firstname,
                lastname,
                username,
                password
        );
        when(repository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        String token = "token";
        when(jwtService.generateToken(Mockito.any(UserDetails.class))).thenReturn(token);

        //when
        AuthenticationResponse response = authenticationService.register(request);

        //then
        assertEquals(token, response.getToken());
        Assertions.assertEquals(firstname, response.getUser().firstname());
        Assertions.assertEquals(lastname, response.getUser().lastname());
    }

    @Test
    void registerTheSameUSer() {

        //given
        String username = "username";
        String password = "password";
        String firstname = "firstname";
        String lastname = "lastname";

        RegisterRequest request = new RegisterRequest(
                firstname,
                lastname,
                username,
                password
        );
        when(repository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(repository.findByUsername(username))
                .thenReturn(Optional.empty())
                .thenReturn(
                        Optional.of(new User(username, password, firstname, lastname, Role.USER))
                );

        String token = "token";
        when(jwtService.generateToken(Mockito.any(UserDetails.class))).thenReturn(token);

        //when
        authenticationService.register(request);

        UserFoundException exception = assertThrows(UserFoundException.class, () ->
                authenticationService.register(request)
        );
        assertEquals(username, exception.getUsername());

    }

    @Test
    void authenticate() {
        //given
        String username = "username";
        String password = "password";
        String firstname = "firstname";
        String lastname = "lastname";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);

        when(repository.findByUsername(username)).thenReturn(Optional.of(user));

        String token = "token";
        when(jwtService.generateToken(Mockito.any(UserDetails.class))).thenReturn(token);

        //when
        AuthenticationResponse response = authenticationService
                .authenticate(new AuthenticationRequest(username, password));

        //then
        assertEquals(token, response.getToken());
        Assertions.assertEquals(firstname, response.getUser().firstname());
        Assertions.assertEquals(lastname, response.getUser().lastname());
    }

}