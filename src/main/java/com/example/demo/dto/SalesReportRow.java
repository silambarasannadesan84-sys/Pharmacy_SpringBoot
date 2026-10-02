package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportRow {

    private String invoiceNumber;

    private LocalDateTime saleDate;

    private Long customerId;

    private BigDecimal amount;

    private BigDecimal profit;

    private String paymentMethod;

    private String status;
}
