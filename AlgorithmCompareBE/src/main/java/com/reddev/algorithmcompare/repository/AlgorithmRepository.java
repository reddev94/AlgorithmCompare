package com.reddev.algorithmcompare.repository;

import com.reddev.algorithmcompare.model.AlgorithmDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AlgorithmRepository extends ReactiveMongoRepository<AlgorithmDocument, String> {
    public Flux<AlgorithmDocument> deleteByIdRequester(String idRequester);
    public Flux<AlgorithmDocument> findByIdRequester(String idRequester);
}
