package com.banking.transaction_service.service;

import com.banking.transaction_service.dto.TransactionRequest;
import com.banking.transaction_service.dto.TransactionResponse;
import com.banking.transaction_service.model.Transaction;
import com.banking.transaction_service.model.TransactionStatus;
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

                boolean accountExists = accountGrpcClient.accountExists(
                                request.getAccountNumber(), request.getCustomerId());

                if (!accountExists) {
                        throw new RuntimeException(
                                        "Account does not exist or does not belong to the customer");
                }

                AccountUpdateResponse updateResp = accountGrpcClient.updateAccountBalance(
                                request.getAccountNumber(),
                                request.getAmount().doubleValue(),
                                "DEPOSIT");

                if (!updateResp.getSuccess()) {
                        String err = updateResp.getErrorMessage();
                        throw new RuntimeException(
                                        err != null && !err.isEmpty() ? err : "Failed to update account balance");
                }

                Transaction transaction = new Transaction();

                transaction.setAccountNumber(
                                request.getAccountNumber());

                transaction.setCustomerId(
                                request.getCustomerId());

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

                boolean accountExists = accountGrpcClient.accountExists(
                                request.getAccountNumber(), request.getCustomerId());

                if (!accountExists) {
                        throw new RuntimeException(
                                        "Account does not exist or does not belong to the customer");
                }

                AccountUpdateResponse updateResp = accountGrpcClient.updateAccountBalance(
                                request.getAccountNumber(),
                                request.getAmount().doubleValue(),
                                "WITHDRAW");

                if (!updateResp.getSuccess()) {
                        String err = updateResp.getErrorMessage();
                        throw new RuntimeException(
                                        err != null && !err.isEmpty() ? err : "Failed to update account balance");
                }

                Transaction transaction = new Transaction();

                transaction.setAccountNumber(
                                request.getAccountNumber());

                transaction.setCustomerId(
                                request.getCustomerId());

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

                response.setCustomerId(
                                transaction.getCustomerId());

                response.setAccountNumber(
                                transaction.getAccountNumber());

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