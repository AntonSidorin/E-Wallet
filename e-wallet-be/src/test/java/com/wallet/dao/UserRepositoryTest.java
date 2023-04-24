package com.wallet.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.wallet.dao.entity.Role;
import com.wallet.dao.entity.User;
import com.wallet.dao.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository repository;

    @Test
    void contextLoads() {
        assertNotNull(em);
    }

    @Test
    void persistAnUser() {
        User user = createUser();
        assertNotNull(user.getUsername());
        String username = user.getUsername();

        em.persist(user);

        assertEquals(username, user.getUsername());
        assertNotNull(user.getFirstname());
        assertNotNull(user.getLastname());
        Assertions.assertEquals(Role.USER, user.getRole());
    }

    @Test
    void verifyRepositoryByPersistingAnUser() {
        User user = createUser();

        assertNotNull(user.getUsername());
        String username = user.getUsername();
        repository.save(user);

        assertEquals(username, user.getUsername());
    }

    @Test
    void findAnUserByUsername() {

        //given
        User user = repository.save(createUser());

        //when
        Optional<User> userByUsername = repository.findByUsername(user.getUsername());

        //then
        assertEquals(user, userByUsername.orElseThrow());
    }

    private User createUser(){
        return new User(
                "username",
                "password",
                "firstname",
                "lastName",
                Role.USER
        );
    }
}
