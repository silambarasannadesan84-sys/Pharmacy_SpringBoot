package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sales")
public class Sale {

    @Id
    private Long id;

    private String invoiceNumber;

    private Long customerId;

    private LocalDateTime saleDate;

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal tax;

    private BigDecimal grandTotal;

    private String paymentMethod;

    private BigDecimal amountReceived;

    private BigDecimal changeAmount;

    private String paymentStatus;

    private String status;
}
