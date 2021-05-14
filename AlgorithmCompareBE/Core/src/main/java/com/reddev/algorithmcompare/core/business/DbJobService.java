package com.reddev.algorithmcompare.core.business;

import com.reddev.algorithmcompare.dao.model.AlgorithmDocument;
import reactor.core.publisher.Flux;

public interface DbJobService {
    public Flux<Integer> deleteOldDbData();
}
