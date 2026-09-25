package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalemItemRequest {

    @NotNull(
            message = "Medicine is required"
    )
    private Long medicineId;

    private String batchNumber;

    @NotNull(
            message = "Quantity is required"
    )
    @Min(
            value = 1,
            message = "Quantity must be atleast 1"
    )
    private Integer quantity;

    private BigDecimal sellingPrice;
}
