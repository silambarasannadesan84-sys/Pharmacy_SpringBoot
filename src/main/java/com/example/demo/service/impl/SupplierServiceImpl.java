package com.example.demo.service.impl;

import com.example.demo.dto.SupplierRequest;
import com.example.demo.dto.SupplierResponse;
import com.example.demo.exception.DuplicateSupplierException;
import com.example.demo.exception.SupplierNotFoundException;
import com.example.demo.model.Supplier;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.service.SequenceGeneratorService;
import com.example.demo.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    private final SequenceGeneratorService sequenceGeneratorService;

    // =====================================================
    // GET ALL
    // =====================================================

    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream().map(this::mapToResponse).toList();
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    public SupplierResponse getSupplierById(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));
        return mapToResponse(supplier);
    }

    // =====================================================
    // CREATE
    // =====================================================

    public SupplierResponse createSupplier(SupplierRequest request) {

        if (supplierRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateSupplierException("Supplier with phone number already exists: " + request.getPhoneNumber());
        }

        if (request.getGstNumber() != null && !request.getGstNumber().trim().isEmpty() && supplierRepository.existsByGstNumber(request.getGstNumber())) {
            throw new DuplicateSupplierException("Supplier with gst number already exist" + request.getGstNumber());
        }

        Long supplierId = sequenceGeneratorService.generateSequence("supplier_sequences");

        Supplier supplier = new Supplier();

        supplier.setId(supplierId);

        supplier.setSupplierName(request.getSupplierName());

        supplier.setContactPerson(request.getContactPerson());

        supplier.setPhoneNumber(request.getPhoneNumber());

        supplier.setEmail(request.getEmail());

        supplier.setAddress(request.getAddress());

        supplier.setCity(request.getCity());

        supplier.setGstNumber(request.getGstNumber());

        supplier.setStatus(request.getStatus());

        Supplier savedSupplier = supplierRepository.save(supplier);

        return mapToResponse(savedSupplier);

    }

    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));

        // -----------------------------------------------
        // PHONE DUPLICATE CHECK
        // -----------------------------------------------
        supplierRepository.findByPhoneNumber(request.getPhoneNumber())
                .ifPresent(existingSupplier -> {
                    if (!existingSupplier.getId().equals(id)) {
                        throw new DuplicateSupplierException("Phone number already belongs to another supplier" + request.getPhoneNumber());
                    }
                });

        // -----------------------------------------------
        // GST DUPLICATE CHECK
        // -----------------------------------------------
        if (request.getGstNumber() != null && !request.getGstNumber().trim().isEmpty()) {
            supplierRepository.findByGstNumber(request.getGstNumber())
                    .ifPresent(existingSupplier -> {
                        if (!existingSupplier.getId().equals(id)) {
                            throw new DuplicateSupplierException("Gst number already belongs to another supplier: " + request.getGstNumber());
                        }
                    });
        }

        supplier.setSupplierName(request.getSupplierName());

        supplier.setContactPerson(request.getContactPerson());

        supplier.setPhoneNumber(request.getPhoneNumber());

        supplier.setEmail(request.getEmail());

        supplier.setAddress(request.getAddress());

        supplier.setCity(request.getCity());

        supplier.setGstNumber(request.getGstNumber());

        supplier.setStatus(request.getStatus());

        Supplier savedSupplier = supplierRepository.save(supplier);

        return mapToResponse(savedSupplier);

    }

    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));

        supplierRepository.delete(supplier);
    }

    // =====================================================
    // SEARCH
    // =====================================================

    public List<SupplierResponse> searchSuppliers(String keyword) {
        if (keyword == null && keyword.trim().isEmpty()) {
            return getAllSuppliers();
        }

        return supplierRepository.findBySupplierNameContainingIgnoreCase(keyword.trim())
                .stream().map(this::mapToResponse).toList();
    }

    private SupplierResponse mapToResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .supplierName(supplier.getSupplierName())
                .contactPerson(supplier.getContactPerson())
                .phoneNumber(supplier.getPhoneNumber())
                .email(supplier.getEmail())
                .address(supplier.getAddress())
                .city(supplier.getCity())
                .gstNumber(supplier.getGstNumber())
                .status(supplier.getStatus())
                .build();
    }
}
