package com.reddev.algorithmcompare.core.service;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.common.domain.entity.AlgorithmDocument;
import com.reddev.algorithmcompare.common.domain.entity.ArrayInfo;
import com.reddev.algorithmcompare.common.repository.AlgorithmRepository;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.core.domain.rest.*;
import com.reddev.algorithmcompare.core.util.CoreUtil;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

@Service
@Log4j2
@RequiredArgsConstructor
public class RestAlgorithmService {

    private final List<Algorithm> algorithms;
    private final AlgorithmRepository algorithmRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Cacheable(value = "executeAlgorithm", key = "'!'.concat(#algorithm.value).concat('!_').concat(#inputArray).concat('_!').concat(#idRequester).concat('!')")
    public ExecuteAlgorithmResponse executeAlgorithm(AlgorithmEnum algorithm, int[] inputArray, long idRequester) {

        return algorithms
                .stream()
                .filter(impl -> impl.getName().equals(algorithm.getValue()))
                .map(element -> {
                    log.info("idRequester created = " + idRequester);
                    long maxMoveExecutionTime = element.execute(inputArray, idRequester);
                    log.info("Algorithm " + algorithm + " executed, maxMoveExecutionTime = " + maxMoveExecutionTime);
                    return ExecuteAlgorithmResponse.builder().idRequester(idRequester).maxExecutionTime(maxMoveExecutionTime).build();
                }).collect(AlgorithmCompareUtil.getCorrectAlgorithm());

    }

    public Mono<DeleteExecuteAlgorithmDataResponse> deleteExecuteAlgorithmData(long idRequester, boolean deleteCache) {

        if(deleteCache) {
            CoreUtil.deleteRedisCache(String.valueOf(idRequester), redisTemplate);
        }

        DeleteExecuteAlgorithmDataResponse response = new DeleteExecuteAlgorithmDataResponse();

        return algorithmRepository.deleteByIdRequester(idRequester)
                .publishOn(AlgorithmCompareUtil.SCHEDULER)
                .reduce(response, (o1, o2) -> {
                    response.setRecordEliminated(1 + o1.getRecordEliminated());
                    return response;
                }).subscribeOn(AlgorithmCompareUtil.SCHEDULER);

    }

    public GenerateArrayResponse generateArray(int length) {

        Set<Integer> uniqueValues = new HashSet<>();
        SecureRandom secureRand = new SecureRandom();

        while (uniqueValues.size() < length) {
            int number = secureRand.nextInt(AlgorithmCompareUtil.ARRAY_MAX_VALUE) + 1;
            uniqueValues.add(number);
        }

        List<Integer> tempList = new ArrayList<>(uniqueValues);
        Collections.shuffle(tempList);

        return new GenerateArrayResponse(tempList.stream().mapToInt(Integer::intValue).toArray());

    }

    @Cacheable(value="availableAlgorithms")
    public GetAlgorithmResponse getAvailableAlgorithms() {

        List<String> availableAlgorithms = Stream.of(AlgorithmEnum.values())
                .map(AlgorithmEnum::getValue)
                .toList();

        return new GetAlgorithmResponse(availableAlgorithms);

    }

    public Flux<GetExecutionDataResponse> getExecutionData(long idRequester, long maxMoveExecutionTime) {

        return algorithmRepository.findByIdRequester(idRequester)
                .publishOn(AlgorithmCompareUtil.SCHEDULER)
                .sort(Comparator.comparing(AlgorithmDocument::getMoveOrder))
                .map(x -> {
                    log.debug("execution data moveOrder = " + x.getMoveOrder());
                    GetExecutionDataResponse response = buildGetExecutionDataResponse(
                            x.getArray(), x.getMoveExecutionTime(), AlgorithmCompareUtil.RESULT_CODE_PROCESSING);
                    log.debug("requester: " + idRequester + " getExecutionData response = " + response);
                    return response;
                })
                .concatWithValues(buildGetExecutionDataResponse(
                        new ArrayInfo[]{}, maxMoveExecutionTime, AlgorithmCompareUtil.RESULT_CODE_OK))
                .delayUntil(data -> {
                    long delay = (data.getMoveExecutionTime() * CoreUtil.MOVE_EXECUTION_TIME_SCALAR) / maxMoveExecutionTime;
                    log.info("emitting data for requester " + idRequester + ", with delay of " + delay + ", getExecutionData response = " + data);
                    return Mono.delay(Duration.ofMillis(delay));
                })
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER);

    }

    private GetExecutionDataResponse buildGetExecutionDataResponse(ArrayInfo[] array, long maxMoveExecutionTime, int resultCodeOk) {

        return GetExecutionDataResponse.builder()
                .array(array)
                .moveExecutionTime(maxMoveExecutionTime)
                .executionStatus(resultCodeOk)
                .build();

    }

}