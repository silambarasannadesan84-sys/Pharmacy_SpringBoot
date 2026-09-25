package com.example.demo.repository;

import com.example.demo.model.SaleItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SaleItemRepository extends MongoRepository<SaleItem, Long> {

    List<SaleItem> findBySaleId(Long saleId);

    void deleteBySaleId(Long id);
}
