package com.example.demo.service;

import com.example.demo.dto.PurchaseRequest;
import com.example.demo.dto.PurchaseResponse;

import java.util.List;

public interface PurchaseService {

    List<PurchaseResponse> getAllPurchases();

    PurchaseResponse getPurchaseById(Long id);

    PurchaseResponse createPurchase(PurchaseRequest request);

    void deletePurchase(Long id);
}
