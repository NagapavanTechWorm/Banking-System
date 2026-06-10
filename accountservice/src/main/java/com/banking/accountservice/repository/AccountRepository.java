package com.banking.accountservice.repository;

import com.banking.accountservice.model.Account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(
            Long accountNumber);

    boolean existsByAccountNumberAndCustomerId(Long accountNumber,
            Long customerId);
}
