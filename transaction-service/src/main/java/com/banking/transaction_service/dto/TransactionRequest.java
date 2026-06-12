package com.banking.transaction_service.dto;

import com.banking.transaction_service.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotNull(message = "Customer ID is required")
    private Long debitorCustomerId;

    @NotNull(message = "Debitor Account number is required")
    private Long debitorAccountNumber;

    @NotNull(message = "Customer ID is required")
    private Long creditorCustomerId;

    @NotNull(message = "Creditor Account number is required")
    private Long creditorAccountNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    private String description;
}
