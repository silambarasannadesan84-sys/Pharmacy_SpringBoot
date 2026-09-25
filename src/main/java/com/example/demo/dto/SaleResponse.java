package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SaleResponse {

    private Long id;

    private String invoiceNumber;

    private Long customerId;

    private LocalDateTime saleDate;

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal tax;

    private BigDecimal grandTotal;

    private String paymentMethod;

    private BigDecimal amountReceived;

    private BigDecimal changeAmount;

    private String paymentStatus;

    private String status;

    private List<SaleItemResponse> items;
}
