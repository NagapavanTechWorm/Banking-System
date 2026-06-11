package com.banking.accountservice.service.grpcserver;

import com.banking.accountservice.repository.AccountRepository;
import com.banking.grpc.account.AccountGrpcServiceGrpc;
import com.banking.grpc.account.AccountUpdateRequest;
import com.banking.grpc.account.AccountUpdateResponse;
import com.banking.grpc.account.AccountValidationRequest;
import com.banking.grpc.account.AccountValidationResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import com.banking.accountservice.model.Account;

import java.math.BigDecimal;
import java.util.Objects;

@GrpcService
@RequiredArgsConstructor
public class AccountGrpcServiceImpl
                extends AccountGrpcServiceGrpc.AccountGrpcServiceImplBase {

        private final AccountRepository accountRepository;

        @Override
        public void validateAccount(
                        AccountValidationRequest request,
                        StreamObserver<AccountValidationResponse> responseObserver) {

                boolean isValidAccount = accountRepository
                                .existsByAccountNumberAndCustomerId(request.getAccountNumber(),
                                                request.getCustomerId());

                AccountValidationResponse response = AccountValidationResponse.newBuilder()
                                .setExists(isValidAccount)
                                .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
        }

        @Override
        public void updateAccount(
                        AccountUpdateRequest request,
                        StreamObserver<AccountUpdateResponse> responseObserver) {

                Account account = accountRepository
                                .findByAccountNumber(
                                                request.getAccountNumber())
                                .orElseThrow(() -> new RuntimeException("Account not found"));

                BigDecimal amount = BigDecimal.valueOf(request.getAmount());

                AccountUpdateResponse.Builder respBuilder = AccountUpdateResponse.newBuilder()
                                .setUpdatedBalance(account.getBalance().doubleValue());

                String txType = request.getTransactionType();
                if ("DEPOSIT".equals(txType)) {
                        account.setBalance(account.getBalance().add(amount));
                        accountRepository.save(account);
                        respBuilder.setSuccess(true)
                                        .setUpdatedBalance(account.getBalance().doubleValue());
                } else if ("WITHDRAW".equals(txType)) {
                        if (account.getBalance().compareTo(amount) < 0) {
                                respBuilder.setSuccess(false)
                                                .setErrorMessage("Insufficient balance")
                                                .setUpdatedBalance(account.getBalance().doubleValue());
                        } else {
                                account.setBalance(account.getBalance().subtract(amount));
                                accountRepository.save(account);
                                respBuilder.setSuccess(true)
                                                .setUpdatedBalance(account.getBalance().doubleValue());
                        }
                } else {
                        respBuilder.setSuccess(false)
                                        .setErrorMessage("Invalid transaction type: " + txType)
                                        .setUpdatedBalance(account.getBalance().doubleValue());
                }

                AccountUpdateResponse response = respBuilder.build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
        }
}