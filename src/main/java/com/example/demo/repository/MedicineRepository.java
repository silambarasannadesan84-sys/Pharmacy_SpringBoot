package com.example.demo.repository;

import com.example.demo.model.Medicine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepositiory extends MongoRepository<Medicine, String> {
}
