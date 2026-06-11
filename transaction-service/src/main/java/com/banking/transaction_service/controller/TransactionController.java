package com.banking.transaction_service.controller;

import com.banking.transaction_service.dto.ApiResponse;
import com.banking.transaction_service.dto.TransactionRequest;
import com.banking.transaction_service.dto.TransactionResponse;
import com.banking.transaction_service.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction API", description = "Endpoints for managing transactions")
public class TransactionController {

        private final TransactionService transactionService;

        @Operation(summary = "Desposit", description = "desposit a new transaction for an existing customer with the provided details")
        @PostMapping("/deposit")
        public ResponseEntity<ApiResponse<TransactionResponse>> DespositTransaction(
                        @Valid @RequestBody TransactionRequest request) {

                TransactionResponse response = transactionService.DespositTransaction(request);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(
                                                ApiResponse.success(
                                                                "Transaction created successfully",
                                                                response));
        }

        @Operation(summary = "Withdraw", description = "withdraw funds from an existing customer's account with the provided details")
        @PostMapping("/withdraw")
        public ResponseEntity<ApiResponse<TransactionResponse>> WithdrawTransaction(
                        @Valid @RequestBody TransactionRequest request) {

                TransactionResponse response = transactionService.WithdrawTransaction(request);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(
                                                ApiResponse.success(
                                                                "Transaction created successfully",
                                                                response));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionById(
                        @PathVariable Long id) {

                TransactionResponse response = transactionService.getTransactionById(id);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Transaction fetched successfully",
                                                response));
        }
}