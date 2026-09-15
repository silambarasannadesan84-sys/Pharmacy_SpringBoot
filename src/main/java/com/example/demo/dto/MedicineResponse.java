package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@Builder
public class MedcineResponse {

    private String Id;

    private String name;

    private String genericName;

    private String manufacturer;

    private String category;

    private String BatchNumber;

    private LocalDate expiryDate;

    private Integer stock;

    private Integer reOrderLevel;

    private BigInteger sellingPrice;

    private 
}
