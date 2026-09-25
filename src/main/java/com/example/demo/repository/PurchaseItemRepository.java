package com.example.demo.repository;

import com.example.demo.model.PurchaseItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchaseItemRepository extends MongoRepository<PurchaseItem, Long> {

    List<PurchaseItem> findByPurchaseId(Long purchaseId);
}
