package com.example.demo.service;

import com.example.demo.dto.SaleRequest;
import com.example.demo.dto.SaleResponse;

import java.util.List;

public interface SaleService {

    List<SaleResponse> getAllSales();

    SaleResponse getSaleById(Long id);

    SaleResponse createSale(SaleRequest request);

    void deleteSale(Long id);
}
