package com.example.demo.service.impl;

import com.example.demo.dto.DashboardResponse;
import com.example.demo.dto.RecentBillResponse;
import com.example.demo.dto.SalesOverviewResponse;
import com.example.demo.dto.TopSellingMedicineResponse;
import com.example.demo.model.Medicine;
import com.example.demo.model.Sale;
import com.example.demo.model.SaleItem;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.repository.SaleItemRepository;
import com.example.demo.repository.SaleRepository;
import com.example.demo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final MedicineRepository medicineRepository;

    public DashboardResponse getDashboard() {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        /*
         * Get today's completed sales.
         */
        List<Sale> todaysSalesList = saleRepository.findAll()
                .stream()
                .filter(sale ->
                        sale.getSaleDate() != null
                && !sale.getSaleDate().isBefore(startOfDay)
                && !sale.getSaleDate().isAfter(endOfDay)
                && "COMPLETED".equalsIgnoreCase(sale.getStatus()))
                .toList();
        /*
         * Today's total sales.
         */
        BigDecimal todaysSales = todaysSalesList.stream()
                .map(Sale::getGrandTotal)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        /*
         * Number of bills generated today.
         */
        long billsGenerated = todaysSalesList.size();

        /*
         * Low-stock medicines.
         */
        List<Medicine> medicines = medicineRepository.findAll();
        long lowStockCount = medicines.stream()
                .filter(medicine -> medicine.getStock() != null
                && medicine.getReOrderLevel() != null && medicine.getStock() <= medicine.getReOrderLevel()).count();

        /*
         * Expiring within 30 days.
         */
        LocalDate expiryLimit = today.plusDays(30);
        long expiringSoonCount = medicines.stream()
                .filter(medicine -> medicine.getExpiryDate() != null)
                .filter(medicine -> !medicine.getExpiryDate().isBefore(today))
                .filter(medicine -> !medicine.getExpiryDate().isAfter(expiryLimit)).count();
        /*
         * Sales chart.
         */
        List<SalesOverviewResponse> salesOverview = buildSalesOverview(todaysSalesList);

        /*
         * Top selling medicines.
         */
        List<TopSellingMedicineResponse> topSellingMedicines = buildTopSellingMedicines();

        /*
         * Recent bills.
         */
        List<RecentBillResponse> recentBills = buildRecentBills();

        return DashboardResponse.builder()
                .todaysSales(todaysSales)
                .billsGenerated((long) billsGenerated)
                .lowStockCount(lowStockCount)
                .expiringSoonCount(expiringSoonCount)
                .salesOverview(salesOverview)
                .topSellingsMedicine(topSellingMedicines)
                .recentBills(recentBills)
                .build();
    }

    private List<SalesOverviewResponse> buildSalesOverview(List<Sale> sales) {
        /*
         * Dashboard UX uses 6-hour intervals:
         *
         * 12 AM
         * 6 AM
         * 12 PM
         * 6 PM
         * 12 AM
         */
        Map<String, BigDecimal> buckets = new LinkedHashMap<>();
        buckets.put("12 AM", BigDecimal.ZERO);
        buckets.put("6 AM", BigDecimal.ZERO);
        buckets.put("12 PM", BigDecimal.ZERO);
        buckets.put("6 PM", BigDecimal.ZERO);

        for (Sale sale: sales) {
            if (sale.getSaleDate() == null || sale.getGrandTotal() == null) {
                continue;
            }

            int hour = sale.getSaleDate().getHour();

            String bucket;

            if (hour < 6) {
                bucket = "12 AM";
            } else if (hour < 12) {
                bucket = "6 AM";
            } else if(hour < 18) {
                bucket = "12 PM";
            } else {
                bucket = "6 PM";
            }

            buckets.put(bucket, buckets.get(bucket).add(sale.getGrandTotal()));
        }
        return buckets.entrySet()
                .stream()
                .map(entry ->
                        SalesOverviewResponse.builder()
                                .time(entry.getKey())
                                .amount(entry.getValue())
                                .build()).toList();
    }

    private List<TopSellingMedicineResponse> buildTopSellingMedicines() {
        List<Sale> completedSales = saleRepository.findAll()
                .stream().filter(sale -> "COMPLETED".equalsIgnoreCase(sale.getStatus()))
                .toList();

        List<Long> saleIds = completedSales.stream()
                .map(Sale::getId).toList();

        if (saleIds.isEmpty()) {
            return List.of();
        }

        List<SaleItem> saleItems = saleItemRepository.findAll()
                .stream().filter(item -> saleIds.contains(item.getSaleId())).toList();

        Map<Long, Long> unitsByMedicine = saleItems.stream()
                .collect(
                        Collectors.groupingBy(
                                SaleItem::getMedicineId,
                                Collectors.summingLong(
                                        item -> item.getQuantity() == null ? 0: item.getQuantity()
                                )
                        )
                );

        Map<Long, String> medicineNames = medicineRepository.findAll()
                .stream().collect(
                        Collectors.toMap(
                                Medicine::getId,
                                Medicine::getName
                        )
                );

        return unitsByMedicine.entrySet()
                .stream().sorted(
                        Map.Entry.<Long, Long>comparingByValue().reversed()
                ).limit(5).map(entry ->
                        TopSellingMedicineResponse.builder()
                                .medicineId(entry.getKey())
                                .medicineName(medicineNames.getOrDefault(entry.getKey(), "Unknown Medicine"))
                                .unitsSold(entry.getValue())
                                .build()
                ).toList();
    }

    private List<RecentBillResponse> buildRecentBills() {

        return saleRepository.findAll()
                .stream().sorted(
                        Comparator.comparing(
                                Sale::getSaleDate,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                ).limit(5)
                .map(sale ->
                        RecentBillResponse.builder()
                                .invoiceNumber(sale.getInvoiceNumber())
                                .customerId(sale.getCustomerId())
                                .amount(sale.getGrandTotal())
                                .paymentMethod(sale.getPaymentMethod())
                                .saleDate(sale.getSaleDate())
                                .status(sale.getStatus())
                                .build()
                ).toList();
    }
}
