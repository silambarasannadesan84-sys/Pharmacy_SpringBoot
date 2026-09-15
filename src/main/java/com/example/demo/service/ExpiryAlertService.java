package com.example.demo.service;

import com.example.demo.dto.ExpiryAlertResponse;

import java.util.List;

public interface ExpiryAlertService {

    List<ExpiryAlertResponse> getExpiredMedicines();

    List<ExpiryAlertResponse> getExpiringWithin30Days();

    List<ExpiryAlertResponse> getExpiringWithin90Days();

    List<ExpiryAlertResponse> searchExpiryAlerts(String keyword);

    ExpiryAlertResponse getExpiryAlertById(Long id);
}
