package com.wallet.dao.repository;

import com.wallet.dao.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    @Query(value = """
            SELECT t 
            FROM Transaction t 
            WHERE t.username = :username AND t.walletId = :walletId 
            ORDER BY t.transactionDate DESC
            """)
    List<Transaction> findByUsernameAndWalletId(
            @Param("username") String username,
            @Param("walletId") String walletId);
}
