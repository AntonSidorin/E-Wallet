package com.wallet.service.mapper.wallet;

import com.wallet.dao.entity.Wallet;
import com.wallet.dto.WalletDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class WalletToWalletDtoMapper implements Function<Wallet, WalletDto> {
    @Override
    public WalletDto apply(Wallet wallet) {
        if(wallet == null){
            return null;
        }
        return new WalletDto(
                wallet.getId(),
                wallet.getName(),
                wallet.getDescription(),
                wallet.getBalance());
    }
}
