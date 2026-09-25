package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleRequest {

    private Long customerId;

    @NotNull(
            message = "Sale must contain atlease one item"
    )
    private List<SalemItemRequest> items;

    @NotNull(message = "Discount is required")
    private BigDecimal discount;

    @NotNull(message = "Tax is required")
    private BigDecimal tax;

    @NotNull(message = "Payment method is required")
    private String paymentMethod;

    @NotNull(message = "Amount Received is required")
    private BigDecimal amountReceived;
}
