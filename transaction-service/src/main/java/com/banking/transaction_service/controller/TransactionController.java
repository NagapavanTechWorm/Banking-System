package com.banking.transaction_service.controller;

import com.banking.transaction_service.dto.ApiResponse;
import com.banking.transaction_service.dto.TransactionDespositRequest;
import com.banking.transaction_service.dto.TransactionResponse;
import com.banking.transaction_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

        private final TransactionService transactionService;

        @PostMapping
        public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(
                        @Valid @RequestBody TransactionDespositRequest request) {

                TransactionResponse response = transactionService.createTransaction(request);

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