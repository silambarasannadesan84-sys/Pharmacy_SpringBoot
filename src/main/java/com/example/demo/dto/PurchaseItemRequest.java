package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PurchaseItemRequest {

    @NotNull(
            message = "medicine ID is required"
    )
    private Long medicineId;

    @NotBlank(
            message = "Batch number is required"
    )
    private String batchNumber;

    @NotNull(
            message = "Expiry date is required"
    )
    private LocalDate expiryDate;

    @NotNull(
            message = "Quantity is required"
    )
    @Min(
            value = 1,
            message = "Quantity atleast must be 1"
    )
    private Integer quantity;

    @NotNull(
            message = "Purchase price is required"
    )
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Purchase price must be greater than 0"
    )
    private BigDecimal purchasePrice;

    @NotNull(
            message = "Selling price is required"
    )
    @DecimalMin(
            value = "0.0", inclusive = false,
            message = "Selling price must be greater than 0"
    )
    private BigDecimal sellingPrice;
}
