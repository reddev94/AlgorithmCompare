package com.reddev.algorithmcompare.core.controller;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.core.domain.rest.DeleteExecuteAlgorithmDataResponse;
import com.reddev.algorithmcompare.core.domain.rest.ExecuteAlgorithmRequest;
import com.reddev.algorithmcompare.core.domain.rest.ExecuteAlgorithmResponse;
import com.reddev.algorithmcompare.core.domain.rest.GetExecutionDataResponse;
import com.reddev.algorithmcompare.core.service.RestAlgorithmService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reactive")
public class RestAlgorithmControllerReactive {

    private final RestAlgorithmService restAlgorithmService;

    @PostMapping(value = "/executeAlgorithm")
    public ResponseEntity<Mono<ExecuteAlgorithmResponse>> executeAlgorithm(@RequestBody ExecuteAlgorithmRequest request) {

        return ResponseEntity.ok(
                restAlgorithmService.executeAlgorithm(AlgorithmEnum.get(request.getAlgorithm()), request.getArray())
                        .subscribeOn(AlgorithmCompareUtil.SCHEDULER)
        );

    }

    @GetMapping(value = "/getExecutionData")
    public ResponseEntity<Flux<GetExecutionDataResponse>> getExecutionData(@RequestParam long idRequester, @RequestParam long maxMoveExecutionTime) {

        return ResponseEntity.ok(
                restAlgorithmService.getExecutionData(idRequester, maxMoveExecutionTime)
                        .subscribeOn(AlgorithmCompareUtil.SCHEDULER)
        );

    }

    @DeleteMapping(value = "/deleteExecuteAlgorithmData")
    public ResponseEntity<Mono<DeleteExecuteAlgorithmDataResponse>> deleteExecuteAlgorithmData(@RequestParam long idRequester) {

        return ResponseEntity.ok(
                restAlgorithmService.deleteExecuteAlgorithmData(idRequester)
                        .subscribeOn(AlgorithmCompareUtil.SCHEDULER)
        );

    }

}
