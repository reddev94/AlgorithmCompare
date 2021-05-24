package com.reddev.algorithmcompare.dao.repository;

import com.reddev.algorithmcompare.dao.model.AlgorithmDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AlgorithmRepository extends ReactiveMongoRepository<AlgorithmDocument, String> {
    public Flux<AlgorithmDocument> deleteByIdRequester(long idRequester);
    public Flux<AlgorithmDocument> findByIdRequester(long idRequester);

    @Query(value = "{idRequester: {$lt: ?0} }")
    public Flux<AlgorithmDocument> deleteWhereIdRequesterLtThanMaxOldTime(long maxOldTime);
}
