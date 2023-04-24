package com.wallet.controller.auth;

import com.wallet.controller.exception.ErrorResult;
import com.wallet.service.auth.AuthenticationResponse;
import com.wallet.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Register & Authentication API")
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(summary = "Register User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "409", description = "User has been already created.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))})
    })
    @PostMapping(value ="/register", produces = "application/json", consumes = "application/json")
    public AuthenticationResponse register(@RequestBody RegisterRequest request){
        return service.register(request);
    }

    @Operation(summary = "Get JWT Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response."),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "403", description = "JWT token is not valid.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "404", description = "User has not been found.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResult.class)))})
    })
    @PostMapping(value="/authenticate", produces = "application/json", consumes = "application/json")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request){
        return service.authenticate(request);
    }


}
