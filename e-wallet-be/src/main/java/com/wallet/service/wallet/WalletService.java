package com.wallet.service.wallet;

import com.wallet.dao.entity.Wallet;
import com.wallet.dao.repository.WalletRepository;
import com.wallet.dto.WalletDto;
import com.wallet.exception.WalletBalanceIsNotZero;
import com.wallet.exception.WalletNotFoundException;
import com.wallet.service.mapper.wallet.WalletDtoToWalletMapper;
import com.wallet.service.mapper.wallet.WalletToWalletDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class WalletService {

    private final WalletRepository walletRepository;

    private final WalletToWalletDtoMapper walletToWalletDtoMapper;

    private final WalletDtoToWalletMapper walletDtoToWalletMapper;

    public List<WalletDto> getWalletsByUsername(String username) {
        return walletRepository.findAllByUsernameOrderByCreatedAsc(username).stream().map(walletToWalletDtoMapper).toList();
    }

    public Optional<WalletDto> getWalletById(String username, String walletId) {
        return walletRepository.findByUsernameAndId(username, walletId).map(walletToWalletDtoMapper);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public WalletDto create(String username, WalletDto walletDto) {
        Wallet wallet = walletDtoToWalletMapper.apply(walletDto);
        wallet.setUsername(username);
        return walletToWalletDtoMapper.apply(walletRepository.save(wallet));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(String username, String walletId) {

        Optional<Wallet> wallet = walletRepository.findByUsernameAndId(username, walletId);
        if (wallet.isPresent()) {
            walletRepository.deleteById(wallet.filter(w -> w.getBalance().compareTo(BigDecimal.ZERO) == 0)
                    .orElseThrow(() -> new WalletBalanceIsNotZero(walletId)).getId());
        } else {
            throw new WalletNotFoundException(username, walletId);
        }
    }

}
