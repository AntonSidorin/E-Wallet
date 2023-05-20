package com.wallet.dao.repository;

import com.wallet.dao.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<Wallet> findByUsernameAndId(String username, String walletId);
    List<Wallet> findAllByUsernameOrderByCreatedAsc(String username);
}
