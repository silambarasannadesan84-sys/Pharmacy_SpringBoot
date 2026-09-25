package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SaleItemResponse {

    private Long id;

    private Long medicineId;

    private String medicineName;

    private String batchNumber;

    private Integer quantity;

    private BigDecimal sellingPrice;

    private BigDecimal totalAmount;
}
