package com.banking.accountservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountBalRequest {
    @NotNull(message = "Account number is required")
    private Long accountNumber;
    @NotNull(message = "Customer ID is required")
    private Long customerId;
}
