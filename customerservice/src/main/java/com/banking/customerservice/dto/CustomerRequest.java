package com.banking.customerservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    @NotBlank(message = "governmentId is required")
    private String governmentId;
}