package com.reddev.algorithmcompare.controller;

import com.reddev.algorithmcompare.controller.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RestAlgorithmControllerReactive {
    public Flux<GetExecutionDataResponse> getExecutionData(String idRequester, long maxMoveExecutionTime);
    public Mono<ExecuteAlgorithmResponse> executeAlgorithm(ExecuteAlgorithmRequest request);
    public Mono<DeleteExecuteAlgorithmDataResponse> deleteExecuteAlgorithmData(String idRequester);
}
