package com.example.demo.repository;

import com.example.demo.model.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SaleRepository extends MongoRepository<Sale, Long> {
}
