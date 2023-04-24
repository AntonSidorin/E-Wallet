package com.wallet.service.wallet.transaction;

import com.wallet.dto.TransactionDto;
import com.wallet.dao.repository.TransactionRepository;
import com.wallet.service.mapper.transaction.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper mapper;

    public List<TransactionDto> getTransactions(String username, String walletId) {
        return transactionRepository.findByUsernameAndWalletId(username, walletId)
                .stream()
                .map(mapper)
                .toList();
    }

}
