package com.example.demo.service.impl;

import com.example.demo.dto.MedicineRequest;
import com.example.demo.dto.MedicineResponse;
import com.example.demo.exception.DuplicateBatchNumberException;
import com.example.demo.exception.MedicineNotFoundException;
import com.example.demo.model.Medicine;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.service.MedicineService;
import com.example.demo.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public List<MedicineResponse> getAllMedicines() {
        List<Medicine> medicines = medicineRepository.findAll();
        return medicines.stream().map(this::mapToResponse).toList();
    }

    private MedicineResponse mapToResponse(Medicine medicine) {
        return MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .genericName(medicine.getGenericName())
                .manufacturer(medicine.getManufacturer())
                .category(medicine.getCategory())
                .batchNumber(medicine.getBatchNumber())
                .expiryDate(medicine.getExpiryDate())
                .stock(medicine.getStock())
                .reOrderLevel(medicine.getReOrderLevel())
                .sellingPrice(medicine.getSellingPrice())
                .status(medicine.getStatus()).build();

    }

    public MedicineResponse createMedicine(MedicineRequest request) {

        if (medicineRepository.existsByBatchNumber((request.getBatchNumber()))) {
            throw new DuplicateBatchNumberException("Batch number already exists: " + request.getBatchNumber());
        }
        Medicine medicine = new Medicine();

        Long medicineId = sequenceGeneratorService.generateSequence("medicine_sequence");
        medicine.setId(medicineId);
        medicine.setName(request.getName());
        medicine.setGenericName(request.getGenericName());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setCategory(request.getCategory());
        medicine.setBatchNumber(request.getBatchNumber());
        medicine.setExpiryDate(request.getExpiryDate());
        medicine.setStock(request.getStock());
        medicine.setReOrderLevel(request.getReOrderLevel());
        medicine.setPurchasePrice(request.getPurchasePrice());
        medicine.setSellingPrice(request.getSellingPrice());
        medicine.setStatus(request.getStatus());
        Medicine savedMedicine = medicineRepository.save(medicine);
        return mapToResponse(savedMedicine);
    }

    public MedicineResponse getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new MedicineNotFoundException("Medicine not found with id: " + id));
        return mapToResponse(medicine);
    }

    public MedicineResponse updateMedicine(Long id, MedicineRequest request) {
        Medicine medicine = medicineRepository.findById(id).orElseThrow(() -> new MedicineNotFoundException("Medicine not found with id: " + id));

        medicineRepository.findByBatchNumber(request.getBatchNumber()).ifPresent(existingMedicine -> {
            if (!existingMedicine.getId().equals(id)) {
                throw new DuplicateBatchNumberException("Batch number already exists: " + request.getBatchNumber());
            }
        });
        medicine.setName(request.getName());
        medicine.setGenericName(request.getGenericName());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setCategory(request.getCategory());
        medicine.setBatchNumber(request.getBatchNumber());
        medicine.setExpiryDate(request.getExpiryDate());
        medicine.setStock(request.getStock());
        medicine.setReOrderLevel(request.getReOrderLevel());
        medicine.setPurchasePrice(request.getPurchasePrice());
        medicine.setSellingPrice(request.getSellingPrice());
        medicine.setStatus(request.getStatus());
        Medicine savedMedicine = medicineRepository.save(medicine);
        return  mapToResponse(savedMedicine);
    }

    public void deleteMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id).orElseThrow(() -> new MedicineNotFoundException("Medicine not found with id: " + id));
        medicineRepository.delete(medicine);
    }

    public List<MedicineResponse> searchMedicines(String name) {
        return medicineRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}
