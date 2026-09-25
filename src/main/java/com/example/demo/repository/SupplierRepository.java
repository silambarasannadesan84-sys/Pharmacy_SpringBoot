package com.example.demo.repository;

import com.example.demo.model.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends MongoRepository<Supplier, Long> {

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByGstNumber(String gstNumber);

    Optional<Supplier> findByPhoneNumber(String phoneNumber);

    Optional<Supplier> findByGstNumber(String gstNumber);

    List<Supplier> findBySupplierNameContainingIgnoreCase(String supplierName);
}
