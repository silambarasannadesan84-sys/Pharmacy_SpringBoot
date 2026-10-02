package com.example.demo.service.impl;

import com.example.demo.dto.SalesReportResponse;
import com.example.demo.dto.SalesReportRow;
import com.example.demo.dto.SalesReportSummary;
import com.example.demo.dto.SalesTrendResponse;
import com.example.demo.model.Sale;
import com.example.demo.model.SaleItem;
import com.example.demo.repository.SaleItemRepository;
import com.example.demo.repository.SaleRepository;
import com.example.demo.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;

    public SalesReportResponse getSalesReport(LocalDate fromDate, LocalDate toDate) {

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date cannot be after to date");
        }

        List<Sale> sales = saleRepository.findAll()
                .stream().filter(sale ->
                        sale.getSaleDate() != null &&
                        !sale.getSaleDate().toLocalDate().isBefore(fromDate) &&
                        !sale.getSaleDate().toLocalDate().isAfter(toDate) &&
                        "COMPLETED".equalsIgnoreCase(sale.getStatus())).toList();

        BigDecimal totalSales = sales.stream()
                .map(Sale::getGrandTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalBills = sales.size();

        BigDecimal totalProfit = calculateProfit(sales);

        BigDecimal averageOrderValue = BigDecimal.ZERO;
        if (totalBills > 0) {
         averageOrderValue = totalSales.divide(BigDecimal.valueOf(totalBills), 2, RoundingMode.HALF_UP);
        }

        SalesReportSummary summary = SalesReportSummary.builder()
                .totalSales(totalSales)
                .totalBills(totalBills)
                .totalProfit(totalProfit)
                .averageOrderValue(averageOrderValue)
                .build();

        List<SalesReportRow> rows = sales.stream()
                .map(this::mapToRow)
                .toList();

        List<SalesTrendResponse> trend = generateSalesTrend(sales);

        return SalesReportResponse.builder()
                .reportType("SALES")
                .fromDate(fromDate)
                .toDate(toDate)
                .summary(summary)
                .salesTrend(trend)
                .rows(rows)
                .build();
    }

    private List<SalesTrendResponse> generateSalesTrend(List<Sale> sales) {

        Map<String, BigDecimal> buckets = new LinkedHashMap<>();
        buckets.put("12 AM", BigDecimal.ZERO);
        buckets.put("4 AM", BigDecimal.ZERO);
        buckets.put("8 AM", BigDecimal.ZERO);
        buckets.put("12 PM", BigDecimal.ZERO);
        buckets.put("4 PM", BigDecimal.ZERO);
        buckets.put("8 PM", BigDecimal.ZERO);

        for(Sale sale: sales) {
            /*
             * If your Sale model stores only LocalDate
             * and not LocalDateTime, all sales will be
             * grouped into the same bucket.
             *
             * See the note below.
             */
            String bucket = "12 PM";
            buckets.put(
                    bucket,
                    buckets.get(bucket).add(sale.getGrandTotal())
            );
        }
        return buckets.entrySet()
                .stream()
                .map(entry ->
                        SalesTrendResponse.builder()
                                .label(entry.getKey())
                                .amount(entry.getValue())
                                .build()).toList();
    }

    private SalesReportRow mapToRow(Sale sale) {
        BigDecimal profit = calculateSaleProfit(sale);

        return SalesReportRow.builder()
                .invoiceNumber(sale.getInvoiceNumber())
                .saleDate(sale.getSaleDate())
                .customerId(sale.getCustomerId())
                .amount(sale.getGrandTotal())
                .profit(profit)
                .paymentMethod(sale.getPaymentMethod())
                .status(sale.getStatus())
                .build();
    }

    private BigDecimal calculateProfit(List<Sale> sales) {
        return sales.stream()
                .map(this::calculateSaleProfit).reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    private BigDecimal calculateSaleProfit(Sale sale) {
        List<SaleItem> items = saleItemRepository.findBySaleId(sale.getId());

        return items.stream()
                .map(item -> {
                    BigDecimal sellingPrice = item.getSellingPrice() == null ? BigDecimal.ZERO: item.getSellingPrice();
                    BigDecimal purchasePrice = item.getPurchasePrice() == null ? BigDecimal.ZERO: item.getPurchasePrice();
                    BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                    BigDecimal profitPerUnit = sellingPrice.subtract(purchasePrice);
                    return profitPerUnit.multiply(quantity);
                }).reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }
}
