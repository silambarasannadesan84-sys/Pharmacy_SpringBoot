package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "medicines")
public class Medicine {
    @Id
    private Long id;

    private String name;

    private String genericName;

    private String manufacturer;

    private String category;

    private String batchNumber;

    private LocalDate expiryDate;

    private Integer stock;

    private Integer reOrderLevel;

    private BigDecimal purchasePrice;

    private BigDecimal sellingPrice;

    private String status;

}
