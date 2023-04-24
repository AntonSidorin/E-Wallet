package com.wallet.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.wallet.dao.entity.Wallet;
import com.wallet.dto.WalletDto;
import com.wallet.service.mapper.wallet.WalletToWalletDtoMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class WalletToWalletDtoMapperTest {

    @Test
    void nullMappingTest() {
        assertNull(new WalletToWalletDtoMapper().apply(null));
    }

    @Test
    void walletMappingTest() {

        //given
        Wallet wallet = new Wallet();
        wallet.setId("id");
        wallet.setName("Test wallet");
        wallet.setDescription("Test description");
        wallet.setBalance(BigDecimal.TEN);

        //when
        WalletDto dto = new WalletToWalletDtoMapper().apply(wallet);

        //then
        assertEquals(dto.id(), wallet.getId());
        assertEquals(dto.name(), wallet.getName());
        assertEquals(dto.description(), wallet.getDescription());
        assertEquals(dto.balance(), wallet.getBalance());

    }
}