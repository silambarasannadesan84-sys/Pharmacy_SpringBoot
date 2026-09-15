package com.example.demo.controller;

import com.example.demo.dto.ExpiryAlertResponse;
import com.example.demo.service.ExpiryAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/expiry-alerts")
@RequiredArgsConstructor
public class ExpiryAlertController {

    private final ExpiryAlertService expiryAlertService;

    @GetMapping("/expired")
    public ResponseEntity<List<ExpiryAlertResponse>> getExpiredMedicines() {
        return ResponseEntity.ok(expiryAlertService.getExpiredMedicines());
    }

    @GetMapping("/within-30-days")
    public ResponseEntity<List<ExpiryAlertResponse>> getExpiringWithin30Days() {
        return ResponseEntity.ok(expiryAlertService.getExpiringWithin30Days());
    }

    @GetMapping("/within-90-days")
    public ResponseEntity<List<ExpiryAlertResponse>> getExpiringWithin90Days() {
        return ResponseEntity.ok(expiryAlertService.getExpiringWithin90Days());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExpiryAlertResponse>> searchExpiryAlerts(String search) {
        return ResponseEntity.ok(expiryAlertService.searchExpiryAlerts(search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpiryAlertResponse> getExpiryAlertById(@PathVariable Long id) {
        return ResponseEntity.ok(expiryAlertService.getExpiryAlertById(id));
    }
}
