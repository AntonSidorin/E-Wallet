package com.wallet.service.mapper.wallet;

import com.wallet.dao.entity.Wallet;
import com.wallet.dto.WalletDto;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class WalletDtoToWalletMapper implements Function<WalletDto, Wallet> {
    @Override
    public Wallet apply(WalletDto walletDto) {
        Wallet wallet = new Wallet();
        wallet.setName(walletDto.name());
        wallet.setDescription(walletDto.description());
        wallet.setBalance(walletDto.balance());
        return wallet;
    }
}
