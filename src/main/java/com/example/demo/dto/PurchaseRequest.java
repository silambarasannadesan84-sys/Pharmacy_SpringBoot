package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseRequest {

    @NotNull(
            message = "Supplier ID is required"
    )
    private Long supplierId;

    @NotNull(
            message = "Purchase date is required"
    )
    private LocalDate purchaseDate;

    private String invoiceNumber;

    @NotEmpty(
            message = "At least one purchase item is required"
    )
    @Valid
    private List<PurchaseItemRequest> items;
}
