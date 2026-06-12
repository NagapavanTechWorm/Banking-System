package com.banking.transaction_service.service;

import com.banking.transaction_service.dto.TransactionRequest;
import com.banking.transaction_service.dto.TransactionResponse;
import com.banking.transaction_service.model.Transaction;
import com.banking.transaction_service.model.TransactionStatus;
import com.banking.transaction_service.model.TransactionType;
import com.banking.transaction_service.repository.TransactionRepository;
import com.banking.transaction_service.service.grpcclient.AccountGrpcClient;
import com.banking.grpc.account.AccountUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

        private final TransactionRepository transactionRepository;
        private final AccountGrpcClient accountGrpcClient;

        public TransactionResponse DespositTransaction(
                        TransactionRequest request) {

                if (request.getTransactionType() != TransactionType.DEPOSIT) {
                        throw new RuntimeException(
                                        "Transaction type must be DEPOSIT for this endpoint");
                }

                boolean accountExists = accountGrpcClient.accountExists(
                                request.getCreditorAccountNumber(), request.getCreditorCustomerId());

                if (!accountExists) {
                        throw new RuntimeException(
                                        "Account does not exist or does not belong to the customer");
                }

                AccountUpdateResponse updateResp = accountGrpcClient.updateAccountBalance(
                                request.getCreditorAccountNumber(),
                                request.getAmount().doubleValue(),
                                "DEPOSIT");

                if (!updateResp.getSuccess()) {
                        String err = updateResp.getErrorMessage();
                        throw new RuntimeException(
                                        err != null && !err.isEmpty() ? err : "Failed to update account balance");
                }

                Transaction transaction = new Transaction();

                transaction.setCreditorAccountNumber(
                                request.getCreditorAccountNumber());

                transaction.setCreditorCustomerId(
                                request.getCreditorCustomerId());

                transaction.setDebitorCustomerId(
                                null); // For deposit, debitor customer is null

                transaction.setDebitorAccountNumber(
                                null); // For deposit, debitor account is null

                transaction.setAmount(
                                request.getAmount());

                transaction.setTransactionType(
                                request.getTransactionType());

                transaction.setDescription(
                                request.getDescription());

                transaction.setStatus(
                                TransactionStatus.SUCCESS);

                Transaction saved = transactionRepository.save(transaction);

                return mapToResponse(saved);
        }

        public TransactionResponse WithdrawTransaction(
                        TransactionRequest request) {

                if (request.getTransactionType() != TransactionType.WITHDRAW) {
                        throw new RuntimeException(
                                        "Transaction type must be WITHDRAW for this endpoint");
                }

                boolean accountExists = accountGrpcClient.accountExists(
                                request.getDebitorAccountNumber(), request.getDebitorCustomerId());

                if (!accountExists) {
                        throw new RuntimeException(
                                        "Account does not exist or does not belong to the customer");
                }

                AccountUpdateResponse updateResp = accountGrpcClient.updateAccountBalance(
                                request.getDebitorAccountNumber(),
                                request.getAmount().doubleValue(),
                                "WITHDRAW");

                if (!updateResp.getSuccess()) {
                        String err = updateResp.getErrorMessage();
                        throw new RuntimeException(
                                        err != null && !err.isEmpty() ? err : "Failed to update account balance");
                }

                Transaction transaction = new Transaction();

                transaction.setCreditorAccountNumber(
                                null); // For withdraw, creditor account is null

                transaction.setCreditorCustomerId(
                                null); // For withdraw, creditor customer is null

                transaction.setDebitorCustomerId(
                                request.getDebitorCustomerId());

                transaction.setDebitorAccountNumber(
                                request.getDebitorAccountNumber());

                transaction.setAmount(
                                request.getAmount());

                transaction.setTransactionType(
                                request.getTransactionType());

                transaction.setDescription(
                                request.getDescription());

                transaction.setStatus(
                                TransactionStatus.SUCCESS);

                Transaction saved = transactionRepository.save(transaction);

                return mapToResponse(saved);
        }

        public TransactionResponse transferTransaction(
                        TransactionRequest request) {

                if (request.getAmount() == null
                                || request.getAmount().signum() <= 0) {

                        throw new RuntimeException(
                                        "Transfer amount must be greater than zero");
                }

                if (request.getDebitorAccountNumber()
                                .equals(request.getCreditorAccountNumber())) {

                        throw new RuntimeException(
                                        "Source and destination accounts cannot be the same");
                }

                boolean creditorExists = accountGrpcClient.accountExists(
                                request.getCreditorAccountNumber(),
                                request.getCreditorCustomerId());

                boolean debitorExists = accountGrpcClient.accountExists(
                                request.getDebitorAccountNumber(),
                                request.getDebitorCustomerId());

                if (!creditorExists || !debitorExists) {

                        throw new RuntimeException(
                                        "One or both accounts do not exist");
                }

                AccountUpdateResponse withdrawResponse = accountGrpcClient.updateAccountBalance(
                                request.getDebitorAccountNumber(),
                                request.getAmount().doubleValue(),
                                "WITHDRAW");

                if (!withdrawResponse.getSuccess()) {

                        throw new RuntimeException(
                                        withdrawResponse.getErrorMessage());
                }

                try {

                        AccountUpdateResponse depositResponse = accountGrpcClient.updateAccountBalance(
                                        request.getCreditorAccountNumber(),
                                        request.getAmount().doubleValue(),
                                        "DEPOSIT");

                        if (!depositResponse.getSuccess()) {

                                throw new RuntimeException(
                                                depositResponse.getErrorMessage());
                        }

                } catch (Exception ex) {

                        // Compensation Transaction
                        accountGrpcClient.updateAccountBalance(
                                        request.getDebitorAccountNumber(),
                                        request.getAmount().doubleValue(),
                                        "DEPOSIT");

                        throw new RuntimeException(
                                        "Transfer failed. Amount refunded to debitor account");
                }

                Transaction transaction = new Transaction();

                transaction.setDebitorCustomerId(
                                request.getDebitorCustomerId());

                transaction.setDebitorAccountNumber(
                                request.getDebitorAccountNumber());

                transaction.setCreditorCustomerId(
                                request.getCreditorCustomerId());

                transaction.setCreditorAccountNumber(
                                request.getCreditorAccountNumber());

                transaction.setAmount(
                                request.getAmount());

                transaction.setTransactionType(
                                TransactionType.TRANSFER);

                transaction.setDescription(
                                request.getDescription());

                transaction.setStatus(
                                TransactionStatus.SUCCESS);

                Transaction saved = transactionRepository.save(
                                transaction);

                return mapToResponse(saved);
        }

        public TransactionResponse getTransactionById(
                        Long transactionId) {

                Transaction transaction = transactionRepository.findById(transactionId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Transaction not found"));

                return mapToResponse(transaction);
        }

        private TransactionResponse mapToResponse(
                        Transaction transaction) {

                TransactionResponse response = new TransactionResponse();

                response.setTransactionId(
                                transaction.getTransactionId());

                response.setDebitorCustomerId(
                                transaction.getDebitorCustomerId());

                response.setDebitorAccountNumber(
                                transaction.getDebitorAccountNumber());

                response.setCreditorCustomerId(
                                transaction.getCreditorCustomerId());

                response.setCreditorAccountNumber(
                                transaction.getCreditorAccountNumber());

                response.setAmount(
                                transaction.getAmount());

                response.setTransactionType(
                                transaction.getTransactionType());

                response.setStatus(
                                transaction.getStatus());

                response.setDescription(
                                transaction.getDescription());

                response.setCreatedAt(
                                transaction.getCreatedAt());

                return response;
        }
}