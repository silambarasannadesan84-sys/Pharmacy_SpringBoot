package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ExpiryAlertResponse {

    private Long id;

    private String name;

    private String genericName;

    private String batchNumber;

    private LocalDate expiryDate;

    private Integer daysLeft;

    private Integer stock;

    private String status;

}
