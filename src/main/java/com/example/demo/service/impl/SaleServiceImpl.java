package com.example.demo.service.impl;

import com.example.demo.dto.SaleItemResponse;
import com.example.demo.dto.SaleRequest;
import com.example.demo.dto.SaleResponse;
import com.example.demo.dto.SalemItemRequest;
import com.example.demo.exception.InvalidSaleException;
import com.example.demo.exception.MedicineNotFoundException;
import com.example.demo.exception.SaleNotFoundException;
import com.example.demo.model.Medicine;
import com.example.demo.model.Sale;
import com.example.demo.model.SaleItem;
import com.example.demo.repository.MedicineRepository;
import com.example.demo.repository.SaleItemRepository;
import com.example.demo.repository.SaleRepository;
import com.example.demo.service.SaleService;
import com.example.demo.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;

    private final SaleItemRepository saleItemRepository;

    private final MedicineRepository medicineRepository;

    private final SequenceGeneratorService sequenceGeneratorService;

    public List<SaleResponse> getAllSales() {
        return saleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public SaleResponse getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with id: " + id));

        return mapToResponse(sale);
    }

    public SaleResponse createSale(SaleRequest request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new InvalidSaleException("Sale must contain at least one item");
        }

        BigDecimal subTotal = BigDecimal.ZERO;

        /*
         * First validate every item before changing
         * any stock.
         */

        List<Medicine> medicines = new ArrayList<>();
        for (SalemItemRequest itemRequest: request.getItems()) {
            Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                    .orElseThrow(() -> new MedicineNotFoundException("Medicine not found with id: " + itemRequest.getMedicineId()));

            if (medicine.getStock() == null || medicine.getStock() < itemRequest.getQuantity()) {
                throw new InvalidSaleException("Insufficient stock for medicine: " + medicine.getName());
            }
            medicines.add(medicine);
        }
        /*
         * Calculate subtotal.
         */

        for (int i = 0; i < request.getItems().size(); i++) {

            SalemItemRequest itemRequest = request.getItems().get(i);
            Medicine medicine = medicines.get(i);

            BigDecimal price = medicine.getSellingPrice();
            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            subTotal = subTotal.add(itemTotal);
        }

        BigDecimal discount = request.getDiscount() == null ? BigDecimal.ZERO: request.getDiscount();

        BigDecimal tax = request.getTax() == null ? BigDecimal.ZERO: request.getTax();

        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSaleException("Discount cannot be negative");
        }
        if (tax.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSaleException("Tax cannot be negative");
        }

        BigDecimal grandTotal = subTotal.subtract(discount).add(tax);
        if (grandTotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSaleException("Grand total cannot be negative");
        }

        BigDecimal amountReceived = request.getAmountReceived();
        if (amountReceived == null && amountReceived.compareTo(grandTotal) < 0) {
            throw new InvalidSaleException("Amount received is less than total amount");
        }

        BigDecimal changeAmount = amountReceived.subtract(grandTotal);

        /*
         * Generate Sale ID.
         */

        Long saleId = sequenceGeneratorService.generateSequence("sale_sequence");
        String invoiceNumber = String.format("INV%06d", saleId);
        Sale sale = Sale.builder()
                .id(saleId)
                .invoiceNumber(invoiceNumber)
                .customerId(request.getCustomerId())
                .saleDate(LocalDateTime.now())
                .subtotal(subTotal)
                .discount(discount)
                .tax(tax)
                .grandTotal(grandTotal)
                .paymentMethod(request.getPaymentMethod())
                .amountReceived(amountReceived)
                .changeAmount(changeAmount)
                .paymentStatus("PAID")
                .status("COMPLETED")
                .build();
        saleRepository.save(sale);

        /*
         * Save items and reduce stock.
         */
        for (int i = 0; i < request.getItems().size(); i++) {
            SalemItemRequest itemRequest = request.getItems().get(i);

            Medicine medicine = medicines.get(i);

            BigDecimal sellingPrice = medicine.getSellingPrice();
            BigDecimal itemTotal = sellingPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            Long itemId = sequenceGeneratorService.generateSequence("sale_item_sequence");

            SaleItem saleItem = SaleItem.builder()
                    .id(itemId)
                    .saleId(saleId)
                    .medicineId(medicine.getId())
                    .medicineName(medicine.getName())
                    .batchNumber(itemRequest.getBatchNumber() != null ? itemRequest.getBatchNumber(): medicine.getBatchNumber())
                    .quantity(itemRequest.getQuantity())
                    .sellingPrice(sellingPrice)
                    .totalAmount(itemTotal)
                    .build();

            saleItemRepository.save(saleItem);

            /*
             * Decrease stock.
             */

            medicine.setStock(medicine.getStock() - itemRequest.getQuantity());
            medicineRepository.save(medicine);
        }
        return mapToResponse(sale);
    }

    public void deleteSale(Long id) {

        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("sale not found with id: " + id));

        /*
         * Restore stock when a sale is deleted.
         */
        List<SaleItem> items = saleItemRepository.findBySaleId(id);

        for (SaleItem item: items) {
            Medicine medicine = medicineRepository.findById(item.getMedicineId()).orElse(null);
            if (medicine != null) {

                int currentStock = medicine.getStock() == null ? 0: medicine.getStock();
                medicine.setStock(currentStock + item.getQuantity());
                medicineRepository.save(medicine);
            }
        }
        saleItemRepository.deleteBySaleId(id);
        saleRepository.delete(sale);
    }

    private SaleResponse mapToResponse(Sale sale) {

        List<SaleItemResponse> items = saleItemRepository.findBySaleId(sale.getId())
                .stream().map(item -> SaleItemResponse.builder()
                        .id(item.getId())
                        .medicineId(item.getMedicineId())
                        .medicineName(item.getMedicineName())
                        .batchNumber(item.getBatchNumber())
                        .quantity(item.getQuantity())
                        .sellingPrice(item.getSellingPrice())
                        .totalAmount(item.getTotalAmount())
                        .build()
                ).toList();
        return SaleResponse.builder()
                .id(sale.getId())
                .invoiceNumber(sale.getInvoiceNumber())
                .customerId(sale.getCustomerId())
                .saleDate(sale.getSaleDate())
                .subtotal(sale.getSubtotal())
                .discount(sale.getDiscount())
                .tax(sale.getTax())
                .grandTotal(sale.getGrandTotal())
                .paymentMethod(sale.getPaymentMethod())
                .amountReceived(sale.getAmountReceived())
                .changeAmount(sale.getChangeAmount())
                .paymentStatus(sale.getPaymentStatus())
                .status(sale.getStatus())
                .items(items)
                .build();
    }
}
