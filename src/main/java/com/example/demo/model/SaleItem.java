package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sale_items")
public class SaleItem {

    @Id
    private Long id;

    private Long saleId;

    private Long medicineId;

    private String medicineName;

    private String batchNumber;

    private Integer quantity;

    private BigDecimal sellingPrice;

    private BigDecimal totalAmount;
}
