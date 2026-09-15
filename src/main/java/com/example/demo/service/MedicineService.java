package com.example.demo.service;

import com.example.demo.dto.MedicineRequest;
import com.example.demo.dto.MedicineResponse;

import java.util.List;

public interface MedicineService {

    List<MedicineResponse> getAllMedicines();

    MedicineResponse getMedicineById(Long id);

    MedicineResponse createMedicine(MedicineRequest request);

    MedicineResponse updateMedicine(Long id, MedicineRequest request);

    void deleteMedicine(Long id);

    List<MedicineResponse> searchMedicines(String name);
}
