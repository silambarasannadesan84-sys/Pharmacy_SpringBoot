package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MedicineRequest {
    @NotBlank(
            message = "Medicine name is required"
    )
    @Size(
         min = 2,
         max = 150,
         message = "Medicine name must be between 2 and 150 characters"
    )
    private String name;

    @Size(
            max = 150,
            message = "Generic name cannot exceed 150 characters"
    )
    private String genericName;

    @NotBlank(
            message = "Manufacturer is required"
    )
    private String manufacturer;

    @NotBlank(
            message = "Category is required"
    )
    private String category;

    @NotBlank(
            message = "Batch number is required"
    )
    private String batchNumber;

    @NotNull(
            message = "Expiry date is required"
    )
    @Future(
            message = "Expiry date must be in the future"
    )
    private LocalDate expiryDate;

    @NotNull(
            message = "Stock is required"
    )
    @Min(
        value = 0,
            message = "Stock cannot be negative"
    )
    private Integer stock;

    @NotNull(
            message = "Reorder level is required"
    )
    @Min(
            value = 0,
            message = "Reorder level cannot be negative"
    )
    private Integer reOrderLevel;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Purchase price must be greater than zero"
    )
    private BigDecimal purchasePrice;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Selling price must be greater than zero"
    )
    private BigDecimal sellingPrice;

    @NotBlank(
            message = "Status is required"
    )
    private String status;
}
