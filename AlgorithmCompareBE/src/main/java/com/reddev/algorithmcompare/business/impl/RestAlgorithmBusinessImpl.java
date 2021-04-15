package com.reddev.algorithmcompare.business.impl;

import com.reddev.algorithmcompare.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.business.RestAlgorithmBusiness;
import com.reddev.algorithmcompare.business.core.Algorithm;
import com.reddev.algorithmcompare.controller.dto.*;
import com.reddev.algorithmcompare.model.AlgorithmDocument;
import com.reddev.algorithmcompare.model.AlgorithmEnum;
import com.reddev.algorithmcompare.model.AlgorithmException;
import com.reddev.algorithmcompare.repository.AlgorithmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.math.MathFlux;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RestAlgorithmBusinessImpl implements RestAlgorithmBusiness {
    Logger logger = LoggerFactory.getLogger(RestAlgorithmBusinessImpl.class);

    @Autowired
    List<Algorithm> algorithms;

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Override
    public Mono<ExecuteAlgorithmResponse> executeAlgorithm(AlgorithmEnum algorithm, int[] inputArray) {
        return Mono.just(
                algorithms
                        .stream()
                        .filter(impl -> impl.getName().equals(algorithm.toString()))
                        .map(element -> {
                            ExecuteAlgorithmResponse res = new ExecuteAlgorithmResponse();
                            try {
                                String idRequester = String.valueOf(AlgorithmCompareUtil.getTimestamp());
                                logger.info("idRequester created = " + idRequester);
                                int result = element.execute(inputArray, idRequester);
                                logger.info("Algorithm execution result = " + result);
                                if (result != AlgorithmCompareUtil.RESULT_CODE_OK) {
                                    res.setResultCode(AlgorithmCompareUtil.RESULT_CODE_KO_GENERIC_ERROR);
                                    res.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_GENERIC_ERROR);
                                } else {
                                    res.setResultCode(AlgorithmCompareUtil.RESULT_CODE_OK);
                                    res.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
                                    res.setIdRequester(idRequester);
                                }
                            } catch (AlgorithmException exception) {
                                logger.error("Exception during executeAlgorithm", exception);
                                res.setResultCode(exception.getCode());
                                res.setResultDescription(exception.getDescription());
                            }
                            return res;
                        }).collect(AlgorithmCompareUtil.getCorrectAlgorithm()))
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER);
    }

    @Override
    public Mono<DeleteExecuteAlgorithmDataResponse> deleteExecuteAlgorithmData(String idRequester) {
        DeleteExecuteAlgorithmDataResponse response = new DeleteExecuteAlgorithmDataResponse();
        return algorithmRepository.deleteByIdRequester(idRequester)
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER)
                .reduce(response, (o1, o2) -> {
                    response.setResultCode(AlgorithmCompareUtil.RESULT_CODE_OK);
                    response.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
                    response.setRecordEliminated(1 + o1.getRecordEliminated());
                    return response;
                }).subscribeOn(AlgorithmCompareUtil.SCHEDULER);
    }

    @Override
    public GenerateArrayResponse generateArray(int length) {
        GenerateArrayResponse response = new GenerateArrayResponse();
        if (length < AlgorithmCompareUtil.ARRAY_MIN_SIZE || length > AlgorithmCompareUtil.ARRAY_MAX_SIZE) {
            logger.info("Invalid array length, should be between " + AlgorithmCompareUtil.ARRAY_MIN_SIZE + " and " + AlgorithmCompareUtil.ARRAY_MAX_SIZE);
            response.setResultCode(AlgorithmCompareUtil.RESULT_CODE_KO_ARRAY_LENGTH_ERROR);
            response.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_ARRAY_LENGTH_ERROR);
        } else {
            int[] array = new int[length];
            Random rand = new Random();
            for (int i = 0; i < length; i++) {
                array[i] = rand.nextInt(AlgorithmCompareUtil.ARRAY_MAX_VALUE);
            }
            response.setResultCode(AlgorithmCompareUtil.RESULT_CODE_OK);
            response.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
            response.setArray(array);
        }
        return response;
    }

    @Override
    public GetAlgorithmResponse getAvailableAlgorithms() {
        GetAlgorithmResponse response = new GetAlgorithmResponse();
        try {
            List<String> availableAlgorithms = Stream.of(AlgorithmEnum.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
            response.setResultCode(AlgorithmCompareUtil.RESULT_CODE_OK);
            response.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
            response.setAvailableAlgorithms(availableAlgorithms);
        } catch (Exception e) {
            logger.error("Exception during getAvailableAlgorithms", e);
            response.setResultCode(AlgorithmCompareUtil.RESULT_CODE_KO_GENERIC_ERROR);
            response.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_GENERIC_ERROR);
        }
        return response;
    }

    @Override
    public Flux<GetExecutionDataResponse> getExecutionData(String idRequester) {
        try {
            return algorithmRepository.findByIdRequester(idRequester)
                    .subscribeOn(AlgorithmCompareUtil.SCHEDULER)
                    .sort(Comparator.comparing(AlgorithmDocument::getMoveOrder))
                    .map(x -> new GetExecutionDataResponse(x.getArray(), x.getMoveExecutionTime(), AlgorithmCompareUtil.RESULT_CODE_OK, AlgorithmCompareUtil.RESULT_DESCRIPTION_OK))
                    .subscribeOn(AlgorithmCompareUtil.SCHEDULER);
        } catch (Exception e) {
            logger.error("Exception during getExecutionData", e);
            GetExecutionDataResponse response = new GetExecutionDataResponse();
            response.setResultCode(AlgorithmCompareUtil.RESULT_CODE_KO_GENERIC_ERROR);
            response.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_GENERIC_ERROR);
            return Flux.just(response)
                    .subscribeOn(AlgorithmCompareUtil.SCHEDULER);
        }
    }

    @Override
    public Mono<GetMaxExecutionTimeResponse> getMaxExecutionTime(String idRequester) {
        try {
            return Mono
                    .from((MathFlux
                            .max(algorithmRepository.findByIdRequester(idRequester)
                                    .subscribeOn(AlgorithmCompareUtil.SCHEDULER)
                                    .map(AlgorithmDocument::getMoveExecutionTime)))
                            .map(maxExecutionTime -> new GetMaxExecutionTimeResponse(maxExecutionTime, AlgorithmCompareUtil.RESULT_CODE_OK, AlgorithmCompareUtil.RESULT_DESCRIPTION_OK)))
                    .subscribeOn(AlgorithmCompareUtil.SCHEDULER);
        } catch (Exception e) {
            logger.error("Exception during getExecutionData", e);
            GetMaxExecutionTimeResponse response = new GetMaxExecutionTimeResponse();
            response.setResultCode(AlgorithmCompareUtil.RESULT_CODE_KO_GENERIC_ERROR);
            response.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_GENERIC_ERROR);
            return Mono.just(response)
                    .subscribeOn(AlgorithmCompareUtil.SCHEDULER);
        }
    }

}
