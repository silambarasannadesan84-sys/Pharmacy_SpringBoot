package com.example.demo.service.impl;

import com.example.demo.dto.PurchaseItemRequest;
import com.example.demo.dto.PurchaseItemResponse;
import com.example.demo.dto.PurchaseRequest;
import com.example.demo.dto.PurchaseResponse;
import com.example.demo.exception.InvalidPurchaseException;
import com.example.demo.exception.MedicineNotFoundException;
import com.example.demo.exception.PurchaseNotFoundException;
import com.example.demo.exception.SupplierNotFoundException;
import com.example.demo.model.Medicine;
import com.example.demo.model.Purchase;
import com.example.demo.model.PurchaseItem;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.repository.PurchaseItemRepository;
import com.example.demo.repository.PurchaseRepository;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.service.PurchaseService;
import com.example.demo.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    private final SupplierRepository supplierRepository;
    private final MedicineRepository medicineRepository;

    private final SequenceGeneratorService sequenceGeneratorService;

    public List<PurchaseResponse> getAllPurchases() {
        return purchaseRepository.findAll()
                .stream().map(this::mapToResponse)
                .toList();
    }

    public PurchaseResponse getPurchaseById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException("Purchase not found with id: " + id));
        return mapToResponse(purchase);
    }

    public PurchaseResponse createPurchase(PurchaseRequest request) {
        //check supplier
        supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + request.getSupplierId()));

        // 2. Generate purchase ID
        Long purchaseId = sequenceGeneratorService.generateSequence("purchase_sequence");

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. Create purchase
        Purchase purchase = Purchase.builder()
                .id(purchaseId)
                .supplierId(request.getSupplierId())
                .invoiceNumber(request.getInvoiceNumber())
                .purchaseDate(request.getPurchaseDate())
                .status("COMPLETED")
                .build();
        // 4. Save purchase first
        purchaseRepository.save(purchase);

        // 5. Process items
        for (PurchaseItemRequest itemRequest: request.getItems()) {

            Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                    .orElseThrow(() -> new MedicineNotFoundException("Medicine not found with id: " + itemRequest.getMedicineId()));

            BigDecimal itemTotal = itemRequest.getPurchasePrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            PurchaseItem item = PurchaseItem.builder()
                    .id(sequenceGeneratorService.generateSequence("purchase_item_sequence"))
                    .purchaseId(purchaseId)
                    .medicineId(itemRequest.getMedicineId())
                    .batchNumber(itemRequest.getBatchNumber())
                    .expiryDate(itemRequest.getExpiryDate())
                    .quantity(itemRequest.getQuantity())
                    .purchasePrice(itemRequest.getPurchasePrice())
                    .sellingPrice(itemRequest.getSellingPrice())
                    .totalAmount(itemTotal)
                    .build();
            purchaseItemRepository.save(item);

            // 6. Increase medicine stock
            int currentStock = medicine.getStock() == null ? 0 : medicine.getStock();
            medicine.setStock(currentStock + itemRequest.getQuantity());
            medicineRepository.save(medicine);
            totalAmount = totalAmount.add(itemTotal);
        }

        // 7. Update purchase total
        purchase.setTotalAmount(totalAmount);
        purchaseRepository.save(purchase);
        return mapToResponse(purchase);

    }

    private PurchaseResponse mapToResponse(Purchase purchase) {

        List<PurchaseItem> items = purchaseItemRepository.findByPurchaseId(purchase.getId());

        List<PurchaseItemResponse> itemResponses = items.stream().map(item ->
            PurchaseItemResponse.builder()
                    .id(item.getId())
                    .medicineId(item.getMedicineId())
                    .batchNumber(item.getBatchNumber())
                    .expiryDate(item.getExpiryDate())
                    .quantity(item.getQuantity())
                    .purchasePrice(item.getPurchasePrice())
                    .sellingPrice(item.getSellingPrice())
                    .totalAmount(item.getTotalAmount())
                    .build()
        ).toList();

        return PurchaseResponse.builder()
                .id(purchase.getId())
                .supplierId(purchase.getSupplierId())
                .invoiceNumber(purchase.getInvoiceNumber())
                .purchaseDate(purchase.getPurchaseDate())
                .totalAmount(purchase.getTotalAmount())
                .status(purchase.getStatus())
                .items(itemResponses)
                .build();
    }

    public void deletePurchase(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException("Purchase not found with id: " + id));

        List<PurchaseItem> items = purchaseItemRepository.findByPurchaseId(id);

        /*
         * Reverse stock before deleting the purchase.
         */

        for (PurchaseItem item : items) {
            Medicine medicine = medicineRepository.findById(item.getMedicineId())
                    .orElse(null);

            if (medicine != null) {
                int currentStock = medicine.getStock() != null ? medicine.getStock() : 0;
                int newStock = currentStock - item.getQuantity();

                if (newStock < 0) {
                    throw new InvalidPurchaseException("Cannot delete purchase because stock would become negative for " +
                            "medicine id: " + item.getMedicineId());
                }
                medicine.setStock(newStock);
                medicineRepository.save(medicine);
            }
        }
        purchaseItemRepository.deleteAll(items);
        purchaseRepository.delete(purchase);
    }
}
