package com.example.demo.service;

import com.example.demo.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {

    List<InventoryResponse> getInventory();

    InventoryResponse getInventoryById(Long medicineId);

    InventoryResponse updateStock(Long medicineId, Integer stock);

}
