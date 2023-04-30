package com.wallet.controller.wallet.transaction;

import com.wallet.controller.exception.ErrorResult;
import com.wallet.dto.TransactionDto;
import com.wallet.service.wallet.WalletFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Wallet Transactions API")
@RequestMapping("/api/v1/wallets/{walletId}")
public class TransactionController {

    private final WalletFacade walletFacade;

    @Operation(summary = "Get Wallet Transactions")
    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "JWT token", required = true, schema = @Schema(type = "String"))
    @Parameter(in = ParameterIn.PATH, name = "walletId", description = "Wallet Id", required = true, schema = @Schema(type = "String"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "403", description = "JWT token is not valid.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "404", description = "Wallet has not been found.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))})
    })
    @GetMapping(value = "/transactions", produces = "application/json")
    public List<TransactionDto> transactions(
            @RequestHeader(name = "Authorization") @NotBlank String token,
            @PathVariable("walletId") @NotBlank String walletId) {
        return walletFacade.getTransactions(token, walletId);
    }

}
