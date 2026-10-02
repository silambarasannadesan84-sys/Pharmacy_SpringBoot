package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportResponse {

    private String reportType;

    private LocalDate fromDate;

    private LocalDate toDate;

    private SalesReportSummary summary;

    private List<SalesTrendResponse> salesTrend;

    private List<SalesReportRow> rows;
}
