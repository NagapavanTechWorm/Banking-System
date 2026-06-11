package com.banking.accountservice.dto;

import com.banking.accountservice.model.AccountType;
import lombok.Data;

@Data
public class AccountBalResponse {

    private Long id;

    private Long customerId;

    private AccountType accountType;

    private Long accountNumber;

    private java.math.BigDecimal balance;
}
