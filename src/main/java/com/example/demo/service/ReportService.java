package com.example.demo.service;

import com.example.demo.dto.SalesReportResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportService {

    SalesReportResponse getSalesReport(LocalDate fromDate, LocalDate toDate);
}
