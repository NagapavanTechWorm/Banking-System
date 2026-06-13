package com.banking.customerservice.controller;

import com.banking.customerservice.dto.CustomerRequest;
import com.banking.customerservice.dto.CustomerResponse;
import com.banking.customerservice.dto.ApiResponse;
import com.banking.customerservice.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customer API", description = "Endpoints for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Create a new customer", description = "Creates a new customer with the provided details")
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @RequestBody CustomerRequest request) {

        CustomerResponse response = customerService.createCustomer(request);

        return ResponseEntity.ok(ApiResponse.success("Customer created", response));
    }

    @Operation(summary = "Get customer by ID", description = "Fetches a customer by their unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(
            @PathVariable Long id) {

        CustomerResponse response = customerService.getCustomerById(id);

        return ResponseEntity.ok(ApiResponse.success("Customer fetched", response));
    }

    @Operation(summary = "Get all customers", description = "Fetches a list of all customers")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {

        List<CustomerResponse> responses = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success("Customers fetched", responses));
    }

    @Operation(summary = "Update customer", description = "Updates an existing customer's details by their ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerRequest request) {

        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Customer updated", response));
    }

    @Operation(summary = "Health check", description = "Checks the health of the customer service")
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Customer Service is running!"));
    }

    @Operation(summary = "Delete customer", description = "Deletes a customer by their unique ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(
            @PathVariable Long id) {

        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully", null));
    }
}