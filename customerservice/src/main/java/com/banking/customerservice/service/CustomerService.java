package com.banking.customerservice.service;

import com.banking.customerservice.dto.CustomerRequest;
import com.banking.customerservice.dto.CustomerResponse;
import com.banking.customerservice.model.Customer;
import com.banking.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

        private final CustomerRepository customerRepository;

        public CustomerResponse createCustomer(CustomerRequest request) {

                Customer customer = Customer.builder()
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .email(request.getEmail())
                                .phoneNumber(request.getPhoneNumber())
                                .governmentId(request.getGovernmentId())
                                .build();

                Customer savedCustomer = customerRepository.save(customer);

                return mapToResponse(savedCustomer);
        }

        public CustomerResponse getCustomerById(Long id) {

                Customer customer = customerRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Customer not found"));

                return mapToResponse(customer);
        }

        public List<CustomerResponse> getAllCustomers() {

                return customerRepository.findAll()
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        public CustomerResponse updateCustomer(Long id,
                        CustomerRequest request) {

                Customer customer = customerRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Customer not found"));

                customer.setFirstName(request.getFirstName());
                customer.setLastName(request.getLastName());
                customer.setEmail(request.getEmail());
                customer.setPhoneNumber(request.getPhoneNumber());
                customer.setGovernmentId(request.getGovernmentId());

                Customer updatedCustomer = customerRepository.save(customer);

                return mapToResponse(updatedCustomer);
        }

        public void deleteCustomer(Long id) {

                Customer customer = customerRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Customer not found"));

                customerRepository.delete(customer);
        }

        private CustomerResponse mapToResponse(Customer customer) {

                return CustomerResponse.builder()
                                .customerId(customer.getCustomerId())
                                .firstName(customer.getFirstName())
                                .lastName(customer.getLastName())
                                .email(customer.getEmail())
                                .phoneNumber(customer.getPhoneNumber())
                                .governmentId(customer.getGovernmentId())
                                .build();
        }
}