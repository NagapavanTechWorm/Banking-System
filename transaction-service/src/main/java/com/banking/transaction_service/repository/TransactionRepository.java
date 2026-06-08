package com.banking.transaction_service.repository;

import com.banking.transaction_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

}