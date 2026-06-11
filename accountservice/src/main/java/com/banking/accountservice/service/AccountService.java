package com.banking.accountservice.service;

import com.banking.accountservice.dto.AccountBalRequest;
import com.banking.accountservice.dto.AccountBalResponse;
import com.banking.accountservice.dto.AccountRequest;
import com.banking.accountservice.dto.AccountResponse;
import com.banking.accountservice.exception.ResourceNotFoundException;
import com.banking.accountservice.model.Account;
import com.banking.accountservice.model.AccountStatus;
import com.banking.accountservice.repository.AccountRepository;
import com.banking.accountservice.service.grpcclient.CustomerGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

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

    public AccountBalResponse getAccountBalance(AccountBalRequest request) {

        Account account = accountRepository
                .findByAccountNumberAndCustomerId(request.getAccountNumber(),
                        request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found for given account number and customer ID"));

        AccountBalResponse response = new AccountBalResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setCustomerId(account.getCustomerId());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        response.setId(account.getAccountId());

        return response;
    }

    private Long generateAccountNumber() {
        long accountNumber;
        do {
            accountNumber = ThreadLocalRandom.current()
                    .nextLong(1_000_000_000L, 10_000_000_000L);
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());

        return accountNumber;
    }
}