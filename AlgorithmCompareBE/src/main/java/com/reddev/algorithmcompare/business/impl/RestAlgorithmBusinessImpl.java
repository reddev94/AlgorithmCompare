package com.reddev.algorithmcompare.business.impl;

import com.reddev.algorithmcompare.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.business.AlgorithmCompareDAO;
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

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RestAlgorithmBusinessImpl implements RestAlgorithmBusiness {
    Logger logger = LoggerFactory.getLogger(RestAlgorithmBusinessImpl.class);

    @Autowired
    private List<Algorithm> algorithms;

    @Autowired
    private AlgorithmCompareDAO algorithmCompareDAO;

    @Override
    public Mono<ExecuteAlgorithmResponse> executeAlgorithm(AlgorithmEnum algorithm, int[] inputArray) {
        ExecuteAlgorithmResponse res = new ExecuteAlgorithmResponse();
        if (inputArray.length > AlgorithmCompareUtil.ARRAY_MAX_SIZE) {
            res.setResultCode(AlgorithmCompareUtil.RESULT_CODE_KO_ARRAY_LENGTH_ERROR);
            res.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_ARRAY_LENGTH_ERROR);
            return Mono.just(res).subscribeOn(AlgorithmCompareUtil.SCHEDULER);
        } else {
            return Mono.just(
                    algorithms
                            .stream()
                            .filter(impl -> impl.getName().equals(algorithm.toString()))
                            .map(element -> {
                                try {
                                    String idRequester = String.valueOf(AlgorithmCompareUtil.getTimestamp());
                                    logger.info("idRequester created = " + idRequester);
                                    long maxMoveExecutionTime = element.execute(inputArray, idRequester);
                                    logger.info("Algorithm " + algorithm + " executed, maxMoveExecutionTime = " + maxMoveExecutionTime);
                                    res.setIdRequester(idRequester);
                                    res.setMaxExecutionTime(maxMoveExecutionTime);
                                    res.setResultCode(AlgorithmCompareUtil.RESULT_CODE_OK);
                                    res.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
                                } catch (AlgorithmException exception) {
                                    logger.error("Exception during executeAlgorithm", exception);
                                    res.setResultCode(exception.getCode());
                                    res.setResultDescription(exception.getDescription());
                                }
                                return res;
                            }).collect(AlgorithmCompareUtil.getCorrectAlgorithm()))
                    .subscribeOn(AlgorithmCompareUtil.SCHEDULER);
        }
    }

    @Override
    public Mono<DeleteExecuteAlgorithmDataResponse> deleteExecuteAlgorithmData(String idRequester) {
        DeleteExecuteAlgorithmDataResponse response = new DeleteExecuteAlgorithmDataResponse();
        return algorithmCompareDAO.deleteDocument(idRequester)
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
            return algorithmCompareDAO.findDocument(idRequester)
                    .sort(Comparator.comparing(AlgorithmDocument::getMoveOrder))
                    .map(x -> {
                        logger.debug("execution data moveOrder = " + x.getMoveOrder());
                        GetExecutionDataResponse response = new GetExecutionDataResponse(x.getArray(), x.getMoveExecutionTime(), AlgorithmCompareUtil.RESULT_CODE_OK, AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
                        logger.info("getExecutionData response = " + response.toString());
                        return response;
                    }).subscribeOn(AlgorithmCompareUtil.SCHEDULER);
        } catch (Exception e) {
            logger.error("Exception during getExecutionData", e);
            GetExecutionDataResponse response = new GetExecutionDataResponse();
            response.setResultCode(AlgorithmCompareUtil.RESULT_CODE_KO_GENERIC_ERROR);
            response.setResultDescription(AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_GENERIC_ERROR);
            return Flux.just(response).subscribeOn(AlgorithmCompareUtil.SCHEDULER);
        }
    }

}
