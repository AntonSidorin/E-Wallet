package com.wallet.controller.wallet;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wallet.controller.exception.ErrorResult;
import com.wallet.dto.WalletDto;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Wallet API")
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletFacade walletFacade;

    @Operation(summary = "Create Wallet")
    @Parameter(in = ParameterIn.HEADER, name = AUTHORIZATION, description = "JWT token", required = true, schema = @Schema(type = "String"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "403", description = "JWT token is not valid.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))})
    })
    @PostMapping(produces = "application/json", consumes = "application/json")
    public WalletDto create(@RequestHeader(name = AUTHORIZATION) @NotBlank String token, @RequestBody @Valid WalletDto walletDto) {
        return walletFacade.create(token, walletDto);
    }

    @Operation(summary = "Get User Wallets")
    @Parameter(in = ParameterIn.HEADER, name = AUTHORIZATION, description = "JWT token", required = true, schema = @Schema(type = "String"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "403", description = "JWT token is not valid.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))})
    })
    @GetMapping(produces = "application/json")
    public List<WalletDto> getWalletsByUsername(@RequestHeader(name = AUTHORIZATION) @NotBlank String token) {
        return walletFacade.getWallets(token);
    }

    @Operation(summary = "Get User Wallet")
    @Parameter(in = ParameterIn.HEADER, name = AUTHORIZATION, description = "JWT token", required = true, schema = @Schema(type = "String"))
    @Parameter(in = ParameterIn.PATH, name = "walletId", description = "Wallet Id", required = true, schema = @Schema(type = "String"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "403", description = "JWT token is not valid.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))})
    })
    @GetMapping(value = "/{walletId}", produces = "application/json")
    public Optional<WalletDto> wallet(
            @RequestHeader(name = AUTHORIZATION) @NotBlank String token,
            @PathVariable("walletId") @NotBlank String walletId
    ) {
        return walletFacade.wallet(token, walletId);
    }

    @Operation(summary = "Get User Wallet's Balance")
    @Parameter(in = ParameterIn.HEADER, name = AUTHORIZATION, description = "JWT token", required = true, schema = @Schema(type = "String"))
    @Parameter(in = ParameterIn.PATH, name = "walletId", description = "Wallet Id", required = true, schema = @Schema(type = "String"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "403", description = "JWT token is not valid.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))})
    })
    @GetMapping(value = "/{walletId}/balance", produces = "application/json")
    public BigDecimal balance(
            @RequestHeader(name = AUTHORIZATION) @NotBlank String token,
            @PathVariable("walletId") @NotBlank String walletId
    ) {
        return walletFacade.balance(token, walletId);
    }

    @Operation(summary = "Top Up  User Wallet")
    @Parameter(in = ParameterIn.HEADER, name = AUTHORIZATION, description = "JWT token", required = true, schema = @Schema(type = "String"))
    @Parameter(in = ParameterIn.PATH, name = "walletId", description = "Wallet Id", required = true, schema = @Schema(type = "String"))
    @Parameter(in = ParameterIn.PATH, name = "amount", description = "Top up amount", required = true, schema = @Schema(type = "BigDecimal"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "403", description = "JWT token is not valid.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))})
    })
    @PatchMapping(value = "/{walletId}/topup/{amount}", produces = "application/json")
    public Optional<WalletDto> topup(
            @RequestHeader(name = AUTHORIZATION) @NotBlank String token,
            @PathVariable("walletId") @NotBlank String walletId,
            @PathVariable("amount")
            @DecimalMin(value = "0", inclusive = false)
            @DecimalMax(value = "10000", inclusive = false, message = "Top up amount must be less than 10000.")
            BigDecimal amount
    ) {
        return walletFacade.topup(token, walletId, amount);
    }

    @Operation(summary = "Withdrawn User Wallet")
    @Parameter(in = ParameterIn.HEADER, name = AUTHORIZATION, description = "JWT token", required = true, schema = @Schema(type = "String"))
    @Parameter(in = ParameterIn.PATH, name = "walletId", description = "Wallet Id", required = true, schema = @Schema(type = "String"))
    @Parameter(in = ParameterIn.PATH, name = "amount", description = "Top up amount", required = true, schema = @Schema(type = "BigDecimal"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "403", description = "JWT token is not valid.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "409", description = "Insufficient wallet funds.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))})
    })
    @PatchMapping(value = "/{walletId}/withdraw/{amount}", produces = "application/json")
    public Optional<WalletDto> withdraw(
            @RequestHeader(name = AUTHORIZATION) @NotBlank String token,
            @PathVariable("walletId") @NotBlank String walletId,
            @PathVariable("amount")
            @DecimalMin(value = "0", inclusive = false)
            @DecimalMax(value = "10000", inclusive = false, message = "Withdraw amount must be less than 10000.")
            BigDecimal amount
    ) {
        return walletFacade.withdraw(token, walletId, amount);
    }

    @Operation(summary = "Transfer Amount From Wallet To Wallet")
    @Parameter(in = ParameterIn.HEADER, name = AUTHORIZATION, description = "JWT token", required = true, schema = @Schema(type = "String"))
    @Parameter(in = ParameterIn.PATH, name = "fromWalletId", description = "From Wallet Id", required = true, schema = @Schema(type = "String"))
    @Parameter(in = ParameterIn.PATH, name = "toWalletId", description = "To Wallet Id", required = true, schema = @Schema(type = "String"))
    @Parameter(in = ParameterIn.PATH, name = "amount", description = "Top up amount", required = true, schema = @Schema(type = "BigDecimal"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "403", description = "JWT token is not valid.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "409", description = "Insufficient from wallet funds.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))})
    })
    @PatchMapping(value = "/{fromWalletId}/transfer/{toWalletId}/{amount}", produces = "application/json")
    public Optional<WalletDto> transfer(
            @RequestHeader(name = AUTHORIZATION) @NotBlank String token,
            @PathVariable("fromWalletId") @NotBlank String fromWalletId,
            @PathVariable("toWalletId") @NotBlank String toWalletId,
            @PathVariable("amount")
            @DecimalMin(value = "0", inclusive = false)
            @DecimalMax(value = "10000", inclusive = false, message = "Transfer amount must be less than 10000.")
            BigDecimal amount
    ) {
        return walletFacade.transfer(token, fromWalletId, toWalletId, amount);
    }

    @Operation(summary = "Delete Wallet")
    @Parameter(in = ParameterIn.HEADER, name = AUTHORIZATION, description = "JWT token", required = true, schema = @Schema(type = "String"))
    @Parameter(in = ParameterIn.PATH, name = "walletId", description = "From Wallet Id", required = true, schema = @Schema(type = "String"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "403", description = "JWT token is not valid.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "409", description = "Wallet has non 0 balance", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))})
    })
    @DeleteMapping(value = "/{walletId}")
    public void delete(@RequestHeader(name = AUTHORIZATION) @NotBlank String token,
                       @PathVariable("walletId") @NotBlank String walletId) {
        walletFacade.delete(token, walletId);
    }
}

