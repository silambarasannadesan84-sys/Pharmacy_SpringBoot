package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopSellingMedicineResponse {

    private Long medicineId;

    private String medicineName;

    private Long unitsSold;
}
