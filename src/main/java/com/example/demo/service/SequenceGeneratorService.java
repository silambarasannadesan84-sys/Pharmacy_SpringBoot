package com.example.demo.service;

import com.example.demo.model.DatabaseSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {

    private final MongoTemplate mongoTemplate;

    public Long generateSequence(String sequenceName) {

        DatabaseSequence sequence =
                mongoTemplate.findAndModify(
                        Query.query(
                                Criteria.where("_id")
                                        .is(sequenceName)
                        ),
                        new Update().inc("sequence", 1),
                        FindAndModifyOptions.options()
                                .returnNew(true)
                                .upsert(true),
                        DatabaseSequence.class
                );

        return sequence.getSequence();
    }
}
