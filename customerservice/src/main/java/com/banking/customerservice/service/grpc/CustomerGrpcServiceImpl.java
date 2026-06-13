package com.banking.customerservice.service.grpc;

import com.banking.customerservice.repository.CustomerRepository;
import com.banking.grpc.customer.CustomerGrpcServiceGrpc;
import com.banking.grpc.customer.CustomerValidationRequest;
import com.banking.grpc.customer.CustomerValidationResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CustomerGrpcServiceImpl
                extends CustomerGrpcServiceGrpc.CustomerGrpcServiceImplBase {

        private final CustomerRepository customerRepository;

        @Override
        public void validateCustomer(
                        CustomerValidationRequest request,
                        StreamObserver<CustomerValidationResponse> responseObserver) {

                boolean exists = customerRepository.existsById(
                                request.getCustomerId());

                CustomerValidationResponse response = CustomerValidationResponse.newBuilder()
                                .setExists(exists)
                                .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
        }
}
