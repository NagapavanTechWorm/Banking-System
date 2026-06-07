package com.banking.accountservice.service;

import com.banking.accountservice.dto.AccountRequest;
import com.banking.accountservice.dto.AccountResponse;
import com.banking.accountservice.exception.ResourceNotFoundException;
import com.banking.accountservice.model.Account;
import com.banking.accountservice.model.AccountStatus;
import com.banking.accountservice.repository.AccountRepository;
import com.banking.accountservice.grpc.CustomerGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerGrpcClient customerGrpcClient;

    public AccountResponse createAccount(AccountRequest request) {

        boolean customerExists = customerGrpcClient.customerExists(
                request.getCustomerId());

        if (!customerExists) {
            throw new ResourceNotFoundException("Customer does not exist");
        }

        Account account = Account.builder()
                .customerId(request.getCustomerId())
                .accountNumber(generateAccountNumber())
                .accountType(request.getAccountType())
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .build();

        Account saved = accountRepository.save(account);

        AccountResponse response = new AccountResponse();
        response.setId(saved.getAccountId());
        response.setCustomerId(saved.getCustomerId());
        response.setAccountType(saved.getAccountType());
        response.setAccountNumber(saved.getAccountNumber());

        return response;
    }

    private String generateAccountNumber() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 20);
    }
}