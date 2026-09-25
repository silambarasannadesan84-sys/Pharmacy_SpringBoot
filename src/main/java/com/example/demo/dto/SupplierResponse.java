package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierResponse {

    private Long id;

    private String supplierName;

    private String contactPerson;

    private String phoneNumber;

    private String email;

    private String address;

    private String city;

    private String gstNumber;

    private String status;

}
