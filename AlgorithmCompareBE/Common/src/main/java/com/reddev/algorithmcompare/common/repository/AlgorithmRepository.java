package com.reddev.algorithmcompare.common.repository;

import com.reddev.algorithmcompare.common.domain.entity.AlgorithmDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AlgorithmRepository extends ReactiveMongoRepository<AlgorithmDocument, String> {

    Flux<AlgorithmDocument> deleteByIdRequester(long idRequester);

    Flux<AlgorithmDocument> findByIdRequester(long idRequester);

    @Query(value = "{idRequester: {$lt: ?0} }")
    Flux<AlgorithmDocument> deleteWhereIdRequesterLtThanMaxOldTime(long maxOldTime);
}
