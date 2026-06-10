package com.banking.accountservice.service.grpcserver;

import com.banking.accountservice.repository.AccountRepository;
import com.banking.grpc.account.AccountGrpcServiceGrpc;
import com.banking.grpc.account.AccountValidationRequest;
import com.banking.grpc.account.AccountValidationResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
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
}