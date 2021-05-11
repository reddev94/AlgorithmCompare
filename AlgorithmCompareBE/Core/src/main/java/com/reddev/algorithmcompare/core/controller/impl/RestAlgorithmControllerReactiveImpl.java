package com.reddev.algorithmcompare.core.controller.impl;

import com.reddev.algorithmcompare.commons.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.commons.model.AlgorithmEnum;
import com.reddev.algorithmcompare.core.business.RestAlgorithmBusiness;
import com.reddev.algorithmcompare.core.controller.RestAlgorithmControllerReactive;
import com.reddev.algorithmcompare.core.controller.dto.DeleteExecuteAlgorithmDataResponse;
import com.reddev.algorithmcompare.core.controller.dto.ExecuteAlgorithmRequest;
import com.reddev.algorithmcompare.core.controller.dto.ExecuteAlgorithmResponse;
import com.reddev.algorithmcompare.core.controller.dto.GetExecutionDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reactive")
public class RestAlgorithmControllerReactiveImpl implements RestAlgorithmControllerReactive {
    Logger logger = LoggerFactory.getLogger(RestAlgorithmControllerReactiveImpl.class);

    @Autowired
    private RestAlgorithmBusiness restAlgorithmBusiness;

    @Override
    @PostMapping(value = "/executeAlgorithm")
    public Mono<ExecuteAlgorithmResponse> executeAlgorithm(@RequestBody ExecuteAlgorithmRequest request) {
        logger.debug("- - - Entering RestAlgorithmControllerReactiveImpl.executeAlgorithm() - - -");
        logger.info("executeAlgorithm request = " + request.toString());
        return restAlgorithmBusiness.executeAlgorithm(AlgorithmEnum.get(request.getAlgorithm()), request.getArray())
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER);
    }

    @Override
    @GetMapping(value = "/getExecutionData")
    public Flux<GetExecutionDataResponse> getExecutionData(@RequestParam String idRequester, @RequestParam long maxMoveExecutionTime) {
        logger.debug("- - - Entering RestAlgorithmControllerReactiveImpl.getExecutionData() - - -");
        logger.info("getExecutionData request: idRequester = " + idRequester + ",  maxMoveExecutionTime = " + maxMoveExecutionTime);
        return restAlgorithmBusiness.getExecutionData(idRequester, maxMoveExecutionTime)
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER);
    }

    @Override
    @DeleteMapping(value = "/deleteExecuteAlgorithmData")
    public Mono<DeleteExecuteAlgorithmDataResponse> deleteExecuteAlgorithmData(@RequestParam String idRequester) {
        logger.debug("- - - Entering RestAlgorithmControllerImpl.deleteExecuteAlgorithmData() - - -");
        logger.info("deleteExecuteAlgorithmData request: idRequester = " + idRequester);
        return restAlgorithmBusiness.deleteExecuteAlgorithmData(idRequester)
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER);
    }

}
