package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class MedicineResponse {

    private Long id;

    private String name;

    private String genericName;

    private String manufacturer;

    private String category;

    private String batchNumber;

    private LocalDate expiryDate;

    private Integer stock;

    private Integer reOrderLevel;

    private BigDecimal sellingPrice;

    private String status;

}
