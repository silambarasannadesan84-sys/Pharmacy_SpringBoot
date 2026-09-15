package com.example.demo.service.impl;

import com.example.demo.dto.ExpiryAlertResponse;
import com.example.demo.exception.MedicineNotFoundException;
import com.example.demo.model.Medicine;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.service.ExpiryAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpiryAlertServiceImpl implements ExpiryAlertService {

    private final MedicineRepository medicineRepository;
    public List<ExpiryAlertResponse> getExpiredMedicines() {

        LocalDate today = LocalDate.now();
        return medicineRepository.findAll().stream()
                .filter(medicine -> medicine.getExpiryDate() != null && medicine.getExpiryDate().isBefore(today))
                .map(this::mapToResponse).toList();
    }

    public List<ExpiryAlertResponse> getExpiringWithin30Days() {

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(30);
        return medicineRepository.findAll()
                .stream().filter(medicine -> medicine.getExpiryDate() != null && !medicine.getExpiryDate().isBefore(today)
                && !medicine.getExpiryDate().isAfter(endDate)).map(this::mapToResponse).toList();
    }

    public List<ExpiryAlertResponse> getExpiringWithin90Days() {

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(90);
        return medicineRepository.findAll()
                .stream().filter(medicine -> medicine.getExpiryDate() != null && !medicine.getExpiryDate().isBefore(today)
                && !medicine.getExpiryDate().isAfter(endDate)).map(this::mapToResponse).toList();
    }

    public ExpiryAlertResponse getExpiryAlertById(Long id){
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new MedicineNotFoundException("Medicine not found: " + id));
        return mapToResponse(medicine);
    }

    public List<ExpiryAlertResponse> searchExpiryAlerts(String keyword) {
        String search = keyword.trim().toLowerCase();
        return medicineRepository.findAll().stream()
                .filter(medicine -> matchesSearch(search, medicine))
                .filter(this::isWithin90DaysOrExpired)
                .map(this::mapToResponse)
                .toList();
    }

    private boolean matchesSearch(String search, Medicine medicine) {
        return contains(medicine.getName(), search) || contains(medicine.getGenericName(), search) || contains(medicine.getBatchNumber(), search);
    }

    private boolean isWithin90DaysOrExpired(Medicine medicine) {
        if (medicine.getExpiryDate() == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(90);
        return !medicine.getExpiryDate().isAfter(endDate);
    }

    private boolean contains(String value, String search) {
        return value != null && value.toLowerCase().contains(search);
    }

    private ExpiryAlertResponse mapToResponse(Medicine medicine) {

        LocalDate today = LocalDate.now();
        String status;
        int daysLeft = (int) ChronoUnit.DAYS.between(today, medicine.getExpiryDate());

        if (daysLeft < 0)
            status = "EXPIRED";
        else if (daysLeft <= 30)
            status = "EXPIRING_SOON";
        else
            status = "EXPIRING_WITHIN_90_DAYS";

        return ExpiryAlertResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .genericName(medicine.getGenericName())
                .batchNumber(medicine.getBatchNumber())
                .expiryDate(medicine.getExpiryDate())
                .daysLeft(daysLeft)
                .stock(medicine.getStock())
                .status(status)
                .build();
    }
}
