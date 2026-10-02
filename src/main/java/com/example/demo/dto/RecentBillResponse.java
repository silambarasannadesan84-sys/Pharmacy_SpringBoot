package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RecentBillResponse {

    private String invoiceNumber;

    private Long customerId;

    private BigDecimal amount;

    private String paymentMethod;

    private LocalDateTime saleDate;

    private String status;
}
