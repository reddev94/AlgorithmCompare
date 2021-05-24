package com.reddev.algorithmcompare.core.business;

import com.reddev.algorithmcompare.core.controller.dto.*;
import com.reddev.algorithmcompare.commons.model.AlgorithmEnum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RestAlgorithmBusiness {

    public Mono<ExecuteAlgorithmResponse> executeAlgorithm(AlgorithmEnum algorithm, int[] inputArray);
    public Mono<DeleteExecuteAlgorithmDataResponse> deleteExecuteAlgorithmData(long idRequester);
    public GenerateArrayResponse generateArray(int length);
    public GetAlgorithmResponse getAvailableAlgorithms();
    public Flux<GetExecutionDataResponse> getExecutionData(long idRequester, long maxMoveExecutionTime);

}
