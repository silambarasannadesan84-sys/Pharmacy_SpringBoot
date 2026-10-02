package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportSummary {

    private BigDecimal totalSales;

    private Long totalBills;

    private BigDecimal totalProfit;

    private BigDecimal averageOrderValue;
}
