package com.example.demo.controller;

import com.example.demo.dto.MedicineRequest;
import com.example.demo.dto.MedicineResponse;
import com.example.demo.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @GetMapping
    public ResponseEntity<List<MedicineResponse>> getAllMedicines() {
        return ResponseEntity.ok(medicineService.getAllMedicines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> getMedicineById(@PathVariable Long id) {
        return ResponseEntity.ok(medicineService.getMedicineById(id));
    }

    /*
     * CREATE MEDICINE
     */
    @PostMapping
    public ResponseEntity<MedicineResponse> createMedicine(@Valid @RequestBody MedicineRequest request) {
        MedicineResponse medicineResponse = medicineService.createMedicine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicineResponse);
    }

    /*
    * UPDATE MEDICINE
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponse> updateMedicine(@PathVariable Long id, @Valid @RequestBody MedicineRequest request) {
        MedicineResponse medicineResponse = medicineService.updateMedicine(id, request);
        return ResponseEntity.ok(medicineResponse);
    }

    /*
    * DELETE MEDICINE
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }

    /*
       * SEARCH MEDICINE
     */
    @GetMapping("/search")
    public ResponseEntity<List<MedicineResponse>> searchMedicines(@RequestParam String name) {
        return ResponseEntity.ok(medicineService.searchMedicines((name)));
    }
}
