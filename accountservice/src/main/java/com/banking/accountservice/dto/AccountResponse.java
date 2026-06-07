package com.banking.accountservice.dto;

import com.banking.accountservice.model.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountResponse {

    private Long id;

    private Long customerId;

    private AccountType accountType;

    private String accountNumber;
}
