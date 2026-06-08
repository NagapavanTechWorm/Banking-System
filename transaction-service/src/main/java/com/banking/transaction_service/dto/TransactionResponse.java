package com.banking.transaction_service.dto;

import com.banking.transaction_service.model.TransactionStatus;
import com.banking.transaction_service.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private Long transactionId;
    private Long accountId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;
}