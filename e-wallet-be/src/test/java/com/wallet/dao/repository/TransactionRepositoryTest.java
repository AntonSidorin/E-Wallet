package com.wallet.dao.repository;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.wallet.dao.entity.Transaction;
import com.wallet.dao.entity.Wallet;
import com.wallet.dao.repository.TransactionRepository;
import com.wallet.dao.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void contextLoads() {
        assertNotNull(em);
    }

    @Test
    void persistTransaction() {
        Transaction transaction = createTransaction(null, null);

        assertNull(transaction.getId());
        em.persist(transaction);
        assertNotNull(transaction.getId());
    }

    @Test
    void verifyRepositoryByPersistingTransaction() {
        Transaction transaction = createTransaction(null, null);

        assertNull(transaction.getId());
        repository.save(transaction);
        assertNotNull(transaction.getId());
    }

    @Test
    void findTransactionsByWalletId() {

        //given
        String username = "username";
        Wallet wallet = new Wallet(null, "name", BigDecimal.TEN, "description", username, null);
        walletRepository.save(wallet);

        Transaction transaction = createTransaction(wallet.getId(), username);
        transaction.setWalletId(wallet.getId());
        repository.save(transaction);

        //when
        List<Transaction> transactions = repository.findByUsernameAndWalletId(username, wallet.getId());

        //then
        assertEquals(1, transactions.size());
        Transaction actualTransaction = transactions.get(0);
        assertEquals(actualTransaction, transaction);
        assertNotNull(actualTransaction.getWalletId());
        assertEquals(username, actualTransaction.getUsername());
    }

    private Transaction createTransaction(String walletId, String username){
        return new Transaction(
                null,
                "transaction description",
                BigDecimal.TEN,
                BigDecimal.ONE,
                now(),
                walletId,
                username
        );
    }
}
