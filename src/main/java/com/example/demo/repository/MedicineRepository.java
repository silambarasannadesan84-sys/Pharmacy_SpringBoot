package com.example.demo.repository;

import com.example.demo.model.Medicine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends MongoRepository<Medicine, Long> {

    boolean existsByBatchNumber(String BatchNumber);

    Optional<Medicine> findByBatchNumber(String BatchNumber);

    List<Medicine> findByNameContainingIgnoreCase(String name);
}
