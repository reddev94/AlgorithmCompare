package com.reddev.algorithmcompare.core.service;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.common.domain.entity.AlgorithmDocument;
import com.reddev.algorithmcompare.common.repository.AlgorithmRepository;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.core.domain.rest.*;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "executeAlgorithm", key = "#algorithm.value.concat('_').concat(#inputArray)")
    public ExecuteAlgorithmResponse executeAlgorithm(AlgorithmEnum algorithm, int[] inputArray) {

        return algorithms
                .stream()
                .filter(impl -> impl.getName().equals(algorithm.getValue()))
                .map(element -> {
                    long idRequester = AlgorithmCompareUtil.getTimestamp();
                    log.info("idRequester created = " + idRequester);
                    long maxMoveExecutionTime = element.execute(inputArray, idRequester);
                    log.info("Algorithm " + algorithm + " executed, maxMoveExecutionTime = " + maxMoveExecutionTime);
                    return ExecuteAlgorithmResponse.builder().idRequester(idRequester).maxExecutionTime(maxMoveExecutionTime).build();
                }).collect(AlgorithmCompareUtil.getCorrectAlgorithm());

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
                    GetExecutionDataResponse response = buildGetExecutionDataResponse(
                            x.getArray(), x.getMoveExecutionTime(), x.getIndexOfSwappedElement(), AlgorithmCompareUtil.RESULT_CODE_PROCESSING);
                    log.debug("requester: " + idRequester + " getExecutionData response = " + response);
                    return response;
                })
                .concatWithValues(buildGetExecutionDataResponse(
                        new int[]{}, maxMoveExecutionTime, -1, AlgorithmCompareUtil.RESULT_CODE_OK))
                .delayUntil(data -> {
                    long delay = (data.getMoveExecutionTime() * 1000) / maxMoveExecutionTime;
                    log.info("emitting data for requester " + idRequester + ", with delay of " + delay + ", getExecutionData response = " + data);
                    return Mono.delay(Duration.ofMillis(delay));
                })
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER);

    }

    private GetExecutionDataResponse buildGetExecutionDataResponse(int[] array, long maxMoveExecutionTime, int indexOfSwappedElement, int resultCodeOk) {

        return GetExecutionDataResponse.builder()
                .array(array)
                .moveExecutionTime(maxMoveExecutionTime)
                .indexOfSwappedElement(indexOfSwappedElement)
                .executionStatus(resultCodeOk)
                .build();

    }

}