package com.example.demo.service;

import com.example.demo.dto.SupplierRequest;
import com.example.demo.dto.SupplierResponse;

import java.util.List;

public interface SupplierService {

    List<SupplierResponse> getAllSuppliers();

    SupplierResponse getSupplierById(Long id);

    SupplierResponse createSupplier(SupplierRequest request);

    SupplierResponse updateSupplier(Long id, SupplierRequest request);

    void deleteSupplier(Long id);

    List<SupplierResponse> searchSuppliers(String keyword);
}
