package com.reddev.algorithmcompare.core.business;

import com.reddev.algorithmcompare.core.model.AlgorithmDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlgorithmCompareDAO {
    public Mono<AlgorithmDocument> saveDocument(int[] array, String idRequester, long moveExecutionTime, long moveOrder, int indexOfSwappedElement);
    public Flux<AlgorithmDocument> deleteDocument(String idRequester);
    public Flux<AlgorithmDocument> findDocument(String idRequester);
    public Flux<AlgorithmDocument> findAll();
}
