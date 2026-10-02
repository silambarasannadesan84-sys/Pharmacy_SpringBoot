package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DashboardResponse {

    private BigDecimal todaysSales;

    private Long billsGenerated;

    private Long lowStockCount;

    private Long expiringSoonCount;

    private List<SalesOverviewResponse> salesOverview;

    private List<TopSellingMedicineResponse> topSellingsMedicine;

    private List<RecentBillResponse> recentBills;
}
