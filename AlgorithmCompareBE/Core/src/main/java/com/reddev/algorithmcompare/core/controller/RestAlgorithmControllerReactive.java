package com.reddev.algorithmcompare.core.controller;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.core.domain.rest.DeleteExecuteAlgorithmDataResponse;
import com.reddev.algorithmcompare.core.domain.rest.ExecuteAlgorithmRequest;
import com.reddev.algorithmcompare.core.domain.rest.ExecuteAlgorithmResponse;
import com.reddev.algorithmcompare.core.domain.rest.GetExecutionDataResponse;
import com.reddev.algorithmcompare.core.service.RestAlgorithmService;
import com.reddev.algorithmcompare.core.util.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reactive")
public class RestAlgorithmControllerReactive {

    private final RestAlgorithmService restAlgorithmService;

    @PostMapping(value = "/executeAlgorithm")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ExecuteAlgorithmResponse> executeAlgorithm(@RequestBody ExecuteAlgorithmRequest request) {

        RequestValidator.checkExecuteAlgorithmRequest(request);
        return Mono.just(
                restAlgorithmService.executeAlgorithm(AlgorithmEnum.get(request.getAlgorithm()), request.getArray(), request.getIdRequester()))
                        .subscribeOn(AlgorithmCompareUtil.SCHEDULER);

    }

    @GetMapping(value = "/getExecutionData")
    @ResponseStatus(HttpStatus.OK)
    public Flux<GetExecutionDataResponse> getExecutionData(@RequestParam long idRequester, @RequestParam long maxMoveExecutionTime) {

        RequestValidator.checkIdRequester(idRequester);
        return restAlgorithmService.getExecutionData(idRequester, maxMoveExecutionTime)
                        .subscribeOn(AlgorithmCompareUtil.SCHEDULER);

    }

    @DeleteMapping(value = "/deleteExecuteAlgorithmData")
    @ResponseStatus(HttpStatus.OK)
    public Mono<DeleteExecuteAlgorithmDataResponse> deleteExecuteAlgorithmData(@RequestParam long idRequester) {

        RequestValidator.checkIdRequester(idRequester);
        return restAlgorithmService.deleteExecuteAlgorithmData(idRequester)
                        .subscribeOn(AlgorithmCompareUtil.SCHEDULER);

    }

}
