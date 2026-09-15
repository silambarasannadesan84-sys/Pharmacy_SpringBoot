package com.example.demo.service.impl;

import com.example.demo.dto.InventoryResponse;
import com.example.demo.exception.MedicineNotFoundException;
import com.example.demo.model.Medicine;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final MedicineRepository medicineRepository;

    @Override
    public List<InventoryResponse> getInventory() {
        return medicineRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public InventoryResponse getInventoryById(Long medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new MedicineNotFoundException("Medicine not found: " + medicineId));
        return this.mapToResponse(medicine);
    }

    public InventoryResponse updateStock(Long medicineId, Integer stock) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new MedicineNotFoundException("Medicine not found: " + medicineId));

        medicine.setStock(stock);

        Medicine updatedMedicine = medicineRepository.save((medicine));
        return mapToResponse(updatedMedicine);
    }

    private InventoryResponse mapToResponse(Medicine medicine) {
        String status;
        if (medicine.getStock() == 0) {
            status = "OUT_OF_STOCK";
        } else if (medicine.getStock() <= medicine.getReOrderLevel()) {
            status = "LOW_STOCK";
        } else {
            status = "IN_STOCK";
        }
        return InventoryResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .genericName(medicine.getGenericName())
                .batchNumber(medicine.getBatchNumber())
                .expiryDate(medicine.getExpiryDate())
                .stock(medicine.getStock())
                .reOrderLevel(medicine.getReOrderLevel())
                .sellingPrice(medicine.getSellingPrice())
                .status(status)
                .build();
    }
}
