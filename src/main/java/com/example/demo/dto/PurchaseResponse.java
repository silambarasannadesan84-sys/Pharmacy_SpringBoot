package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PurchaseResponse {

    private Long id;

    private Long supplierId;

    private String invoiceNumber;

    private LocalDate purchaseDate;

    private BigDecimal totalAmount;

    private String status;

    private List<PurchaseItemResponse> items;
}
