package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "suppliers")
public class Supplier {

    @Id
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
