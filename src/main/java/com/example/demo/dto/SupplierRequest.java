package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SupplierRequest {

    @NotBlank(
            message = "Supplier name is required"
    )
    @Size(
            max = 100,
            message = "Supplier name cannot exceed 100 characters"
    )
    private String supplierName;

    @NotBlank(
            message = "Contact person is required"
    )
    @Size(
            max = 100,
            message = "Contact person cannot exceed 100 characters"
    )
    private String contactPerson;

    @NotBlank(
            message = "Phone number is required"
    )
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phoneNumber;

    @Email(
            message = "Invalid email address"
    )
    private String email;

    @NotBlank(
            message = "Address is required"
    )
    private String address;

    @NotBlank(
            message = "City is required"
    )
    private String city;

    private String gstNumber;

    @NotBlank(
            message = "Status is required"
    )
    private String status;
}
