package com.banking.accountservice.dto;

import com.banking.accountservice.model.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;
}