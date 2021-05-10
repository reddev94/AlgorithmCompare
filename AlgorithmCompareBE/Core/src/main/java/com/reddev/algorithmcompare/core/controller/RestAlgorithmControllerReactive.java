package com.reddev.algorithmcompare.core.controller;

import com.reddev.algorithmcompare.core.controller.dto.DeleteExecuteAlgorithmDataResponse;
import com.reddev.algorithmcompare.core.controller.dto.ExecuteAlgorithmRequest;
import com.reddev.algorithmcompare.core.controller.dto.ExecuteAlgorithmResponse;
import com.reddev.algorithmcompare.core.controller.dto.GetExecutionDataResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RestAlgorithmControllerReactive {
    public Flux<GetExecutionDataResponse> getExecutionData(String idRequester, long maxMoveExecutionTime);
    public Mono<ExecuteAlgorithmResponse> executeAlgorithm(ExecuteAlgorithmRequest request);
    public Mono<DeleteExecuteAlgorithmDataResponse> deleteExecuteAlgorithmData(String idRequester);
}
