package com.wallet.dao.repository;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.wallet.dao.entity.Wallet;
import com.wallet.dao.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
class WalletRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private WalletRepository repository;

    @Test
    void contextLoads() {
        assertNotNull(em);
    }

    @Test
    void persistWallet() {
        LocalDateTime created = now().minusMinutes(1);
        Wallet wallet = createWallet();
        wallet.setCreated(created);

        assertNull(wallet.getId());
        em.persist(wallet);
        assertNotNull(wallet.getId());
        assertNotEquals(created, wallet.getCreated());
    }

    @Test
    void verifyRepositoryByPersistingWallet() {

        Wallet wallet = createWallet();

        assertNull(wallet.getId());
        repository.save(wallet);
        assertNotNull(wallet.getId());
        assertNotNull(wallet.getName());
        assertNotNull(wallet.getUsername());
        assertNotNull(wallet.getBalance());
        assertNotNull(wallet.getCreated());
    }

    @Test
    void findWalletById() {

        //given
        Wallet wallet = repository.save(createWallet());

        //when
        Optional<Wallet> walletByUsername = repository.findById(wallet.getId());

        //then
        assertEquals(wallet, walletByUsername.orElseThrow());
    }

    private Wallet createWallet() {
        return new Wallet(
                null,
                "wallet name",
                BigDecimal.TEN,
                "wallet description",
                "username",
                null
        );
    }
}
