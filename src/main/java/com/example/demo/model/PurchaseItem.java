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
@Document(collection = "purchase_items")
public class PurchaseItem {

    @Id
    private Long id;

    private Long purchaseId;

    private Long medicineId;

    private String batchNumber;

    private LocalDate expiryDate;

    private Integer quantity;

    private BigDecimal purchasePrice;

    private BigDecimal sellingPrice;

    private BigDecimal totalAmount;
}
