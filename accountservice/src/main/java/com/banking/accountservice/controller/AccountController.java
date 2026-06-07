package com.banking.accountservice.controller;

import com.banking.accountservice.dto.AccountRequest;
import com.banking.accountservice.dto.AccountResponse;
import com.banking.accountservice.dto.ApiResponse;
import com.banking.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account API", description = "Endpoints for managing accounts")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Create a new account", description = "Creates a new account for an existing customer with the provided details")
    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @Valid @RequestBody AccountRequest request) {

        AccountResponse response = accountService.createAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Account created successfully",
                                response));
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Account Service is up and running!";
    }
}