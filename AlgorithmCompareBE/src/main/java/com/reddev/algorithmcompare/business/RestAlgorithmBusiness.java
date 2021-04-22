package com.reddev.algorithmcompare.business;

import com.reddev.algorithmcompare.controller.dto.*;
import com.reddev.algorithmcompare.model.AlgorithmEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RestAlgorithmBusiness {

    public Mono<ExecuteAlgorithmResponse> executeAlgorithm(AlgorithmEnum algorithm, int[] inputArray);
    public Mono<DeleteExecuteAlgorithmDataResponse> deleteExecuteAlgorithmData(String idRequester);
    public GenerateArrayResponse generateArray(int length);
    public GetAlgorithmResponse getAvailableAlgorithms();
    public Flux<GetExecutionDataResponse> getExecutionData(String idRequester);

}
