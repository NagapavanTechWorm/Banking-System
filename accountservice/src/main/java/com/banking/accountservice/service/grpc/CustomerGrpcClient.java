package com.banking.accountservice.grpc;

import com.banking.grpc.customer.CustomerGrpcServiceGrpc;
import com.banking.grpc.customer.CustomerValidationRequest;
import com.banking.grpc.customer.CustomerValidationResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class CustomerGrpcClient {

    @GrpcClient("customer-service")
    private CustomerGrpcServiceGrpc.CustomerGrpcServiceBlockingStub stub;

    public boolean customerExists(Long customerId) {

        CustomerValidationResponse response = stub.validateCustomer(
                CustomerValidationRequest.newBuilder()
                        .setCustomerId(customerId)
                        .build());

        return response.getExists();
    }
}