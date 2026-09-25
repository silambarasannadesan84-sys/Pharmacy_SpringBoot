package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PurchaseItemResponse {

    private Long id;

    private Long medicineId;

    private String batchNumber;

    private LocalDate expiryDate;

    private Integer quantity;

    private BigDecimal purchasePrice;

    private BigDecimal sellingPrice;

    private BigDecimal totalAmount;
}
