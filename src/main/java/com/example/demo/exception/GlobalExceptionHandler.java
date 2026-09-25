package com.example.demo.exception;

import com.example.demo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedicineNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMedicineNotFound(MedicineNotFoundException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Medicine Not Found");
        response.put("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateBatchNumberException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateBatch(DuplicateBatchNumberException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Duplicate Batch Number");
        response.put("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse response = ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errors(errors).build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSupplierNotFound(SupplierNotFoundException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "SUPPLIER_NOT_FOUND");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(DuplicateSupplierException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateSupplier(DuplicateSupplierException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "DUPLICATE_SUPPLIER");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(PurchaseNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePurchaseNotFound(PurchaseNotFoundException ex) {
        Map<String, String> response = new HashMap<>();

        response.put("error", "PURCHASE_NOT_FOUND");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidPurchaseException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPurchase(InvalidPurchaseException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "INVALID_PURCHASE");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(SaleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSaleNotFound(SaleNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "SALE_NOT_FOUND");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidSaleException.class)
    public ResponseEntity<Map<String, String>> handleInvalidSale(InvalidSaleException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "INVALID_SALE");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
