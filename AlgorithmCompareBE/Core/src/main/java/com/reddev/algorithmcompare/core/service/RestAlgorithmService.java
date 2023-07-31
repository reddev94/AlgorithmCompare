package com.reddev.algorithmcompare.core.service;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.common.domain.entity.AlgorithmDocument;
import com.reddev.algorithmcompare.common.domain.exception.AlgorithmException;
import com.reddev.algorithmcompare.common.repository.AlgorithmRepository;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.core.domain.rest.*;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
@RequiredArgsConstructor
public class RestAlgorithmService {

    private final List<Algorithm> algorithms;
    private final AlgorithmRepository algorithmRepository;

    @Cacheable(value = "executeAlgorithm")
    public Mono<ExecuteAlgorithmResponse> executeAlgorithm(AlgorithmEnum algorithm, int[] inputArray) {

        if (inputArray.length > AlgorithmCompareUtil.ARRAY_MAX_SIZE) {
            log.error("Invalid array length, should be between " + AlgorithmCompareUtil.ARRAY_MIN_SIZE + " and " + AlgorithmCompareUtil.ARRAY_MAX_SIZE);
            throw new AlgorithmException(AlgorithmCompareUtil.RESULT_CODE_KO_ARRAY_LENGTH_ERROR, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_ARRAY_LENGTH_ERROR);
        }

        return Mono.just(
                        algorithms
                                .stream()
                                .filter(impl -> impl.getName().equals(algorithm.getValue()))
                                .map(element -> {
                                    long idRequester = AlgorithmCompareUtil.getTimestamp();
                                    log.info("idRequester created = " + idRequester);
                                    long maxMoveExecutionTime = element.execute(inputArray, idRequester);
                                    log.info("Algorithm " + algorithm + " executed, maxMoveExecutionTime = " + maxMoveExecutionTime);
                                    return ExecuteAlgorithmResponse.builder().idRequester(idRequester).maxExecutionTime(maxMoveExecutionTime).build();
                                }).collect(AlgorithmCompareUtil.getCorrectAlgorithm()))
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER);

    }

    @CacheEvict(value = "executeAlgorithm", allEntries = true)
    public Mono<DeleteExecuteAlgorithmDataResponse> deleteExecuteAlgorithmData(long idRequester) {

        DeleteExecuteAlgorithmDataResponse response = new DeleteExecuteAlgorithmDataResponse();

        return algorithmRepository.deleteByIdRequester(idRequester)
                .publishOn(AlgorithmCompareUtil.SCHEDULER)
                .reduce(response, (o1, o2) -> {
                    response.setRecordEliminated(1 + o1.getRecordEliminated());
                    return response;
                }).subscribeOn(AlgorithmCompareUtil.SCHEDULER);

    }

    public GenerateArrayResponse generateArray(int length) {

        if (length < AlgorithmCompareUtil.ARRAY_MIN_SIZE || length > AlgorithmCompareUtil.ARRAY_MAX_SIZE) {
            log.error("Invalid array length, should be between " + AlgorithmCompareUtil.ARRAY_MIN_SIZE + " and " + AlgorithmCompareUtil.ARRAY_MAX_SIZE);
            throw new AlgorithmException(AlgorithmCompareUtil.RESULT_CODE_KO_ARRAY_LENGTH_ERROR, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_ARRAY_LENGTH_ERROR);
        }

        List<Integer> list = new ArrayList<>();
        Random rand = new Random(Double.doubleToLongBits(Math.random()));
        while (list.size() < length) {
            int number = rand.nextInt(AlgorithmCompareUtil.ARRAY_MAX_VALUE) + 1;
            if (!list.contains(number)) {
                list.add(number);
            }
        }

        return new GenerateArrayResponse(list.stream().mapToInt(Integer::intValue).toArray());

    }

    @Cacheable(value="availableAlgorithms")
    public GetAlgorithmResponse getAvailableAlgorithms() {

        List<String> availableAlgorithms = Stream.of(AlgorithmEnum.values())
                .map(AlgorithmEnum::getValue)
                .collect(Collectors.toList());

        return new GetAlgorithmResponse(availableAlgorithms);

    }

    public Flux<GetExecutionDataResponse> getExecutionData(long idRequester, long maxMoveExecutionTime) {

        return algorithmRepository.findByIdRequester(idRequester)
                .publishOn(AlgorithmCompareUtil.SCHEDULER)
                .sort(Comparator.comparing(AlgorithmDocument::getMoveOrder))
                .map(x -> {
                    log.debug("execution data moveOrder = " + x.getMoveOrder());
                    //TODO aggiungere il campo con il codice ritorno processing = 1
                    GetExecutionDataResponse response = GetExecutionDataResponse.builder()
                            .array(x.getArray())
                            .moveExecutionTime(x.getMoveExecutionTime())
                            .indexOfSwappedElement(x.getIndexOfSwappedElement())
                            .build();
                    log.debug("requester: " + idRequester + " getExecutionData response = " + response);
                    return response;
                })
                //TODO aggiungere il campo con il codice ritorno processing = 0 (finito)
                .concatWithValues(GetExecutionDataResponse.builder()
                        .array(new int[]{})
                        .moveExecutionTime(maxMoveExecutionTime)
                        .indexOfSwappedElement(-1)
                        .build())
                .delayUntil(data -> {
                    long delay = (data.getMoveExecutionTime() * 1000) / maxMoveExecutionTime;
                    log.info("emitting data for requester " + idRequester + ", with delay of " + delay + ", getExecutionData response = " + data);
                    return Mono.delay(Duration.ofMillis(delay));
                })
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER);

    }

}