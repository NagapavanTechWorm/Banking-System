package com.banking.transaction_service.service.grpcclient;

import com.banking.grpc.account.AccountGrpcServiceGrpc;
import com.banking.grpc.account.AccountUpdateRequest;
import com.banking.grpc.account.AccountUpdateResponse;
import com.banking.grpc.account.AccountValidationRequest;
import com.banking.grpc.account.AccountValidationResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class AccountGrpcClient {

    @GrpcClient("account-service")
    private AccountGrpcServiceGrpc.AccountGrpcServiceBlockingStub stub;

    public boolean accountExists(Long accountNumber, Long customerId) {

        AccountValidationResponse response = stub.validateAccount(
                AccountValidationRequest.newBuilder()
                        .setAccountNumber(accountNumber)
                        .setCustomerId(customerId)
                        .build());

        return response.getExists();
    }

    public AccountUpdateResponse updateAccountBalance(
            Long accountNumber,
            Double amount,
            String transactionType) {

        AccountUpdateResponse response = stub.updateAccount(
                AccountUpdateRequest.newBuilder()
                        .setAccountNumber(accountNumber)
                        .setAmount(amount)
                        .setTransactionType(transactionType)
                        .build());

        return response;
    }

}
