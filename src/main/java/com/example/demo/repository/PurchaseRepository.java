package com.example.demo.repository;

import com.example.demo.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseRepository extends MongoRepository<Purchase, Long> {

}
