package com.reddev.algorithmcompare.core.business;

import reactor.core.publisher.Mono;

public interface DbJobService {
    public Mono<DeleteOldDataJobResponse> deleteOldDbData();
}
