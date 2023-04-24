package com.wallet.service.wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wallet.dao.entity.Wallet;
import com.wallet.dao.repository.WalletRepository;
import com.wallet.dto.WalletDto;
import com.wallet.exception.WalletBalanceIsNotZero;
import com.wallet.service.mapper.wallet.WalletDtoToWalletMapper;
import com.wallet.service.mapper.wallet.WalletToWalletDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Spy
    private WalletToWalletDtoMapper walletToWalletDtoMapper = new WalletToWalletDtoMapper();

    @Spy
    private WalletDtoToWalletMapper walletDtoToWalletMapper = new WalletDtoToWalletMapper();

    @InjectMocks
    private WalletService walletService;

    @Test
    void getWalletByIdTest() {
        //given
        String username = "username";
        String walletId = "walletId";

        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.ZERO);
        when(walletRepository.findByUsernameAndId(username, walletId)).thenReturn(Optional.of(wallet));

        //when
        Optional<WalletDto> actualWalletDto = walletService.getWalletById(username, walletId);

        //then
        assertEquals(Optional.of(walletToWalletDtoMapper.apply(wallet)), actualWalletDto);
    }

    @Test
    void create() {
        //given
        String username = "username";
        WalletDto walletDto = new WalletDto(null, "Wallet name", "Wallet description", BigDecimal.TEN);
        when(walletRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArguments()[0]);

        //when
        WalletDto actualWalletDto = walletService.create(username, walletDto);

        //then
        assertEquals(walletDto, actualWalletDto);
    }

    @Test
    void deleteWithZeroBalance() {
        //given
        String username = "username";
        String walletId = "walletId";
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.ZERO);
        when(walletRepository.findByUsernameAndId(username, walletId)).thenReturn(Optional.of(wallet));

        //when
        walletService.delete(username, walletId);

        //then
        verify(walletRepository, atMostOnce()).deleteById(walletId);
    }

    @Test
    void deleteWithNonZeroBalance() {
        //given
        String username = "username";
        String walletId = "walletId";
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.TEN);
        when(walletRepository.findByUsernameAndId(username, walletId)).thenReturn(Optional.of(wallet));

        //when
        WalletBalanceIsNotZero exception = assertThrows(WalletBalanceIsNotZero.class, () ->
                walletService.delete(username, walletId)
        );

        assertEquals(walletId, exception.getWalletId());
    }

}