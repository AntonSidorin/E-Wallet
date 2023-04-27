package com.wallet.security;

import static com.wallet.security.JwtService.BEARER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.wallet.dao.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void generateTokenTest(){

        //given
        String username = "username";

        User user = new User();
        user.setUsername(username);

        //when
        String token = BEARER + jwtService.generateToken(user);

        //then
        assertTrue(jwtService.isTokenValid(token, user));
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void incorrectUserForTokenTest(){

        //given
        String username = "username";

        User user = new User();
        user.setUsername(username);

        //when
        String token = BEARER + jwtService.generateToken(user);

        //then
        assertFalse(jwtService.isTokenValid(token, new User()));
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void invalidTokenTest(){

        //given
        String username = "username";

        User user = new User();
        user.setUsername(username);

        //when
        String token = jwtService.generateToken(user);

        //then
        assertFalse(jwtService.isTokenValid(token, user));

    }

}
