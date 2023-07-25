package com.reddev.algorithmcompare.core.service;

import com.reddev.algorithmcompare.common.repository.AlgorithmRepository;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.core.domain.business.DeleteOldDataJobResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class DbJobService {

    @Value("${job.seconds.old.data}")
    private long jobSecondsOldData;

    private final AlgorithmRepository algorithmRepository;

    public Mono<DeleteOldDataJobResponse> deleteOldDbData() {

        DeleteOldDataJobResponse response = new DeleteOldDataJobResponse();
        long maxOldData = AlgorithmCompareUtil.getTimestampMinusSeconds(jobSecondsOldData);
        log.info("maxOldData = " + maxOldData);

        return algorithmRepository.deleteWhereIdRequesterLtThanMaxOldTime(maxOldData)
                .publishOn(AlgorithmCompareUtil.SCHEDULER)
                .reduce(response, (o1, o2) -> {
                    response.setDeletedElement(1 + o1.getDeletedElement());
                    return response;
                }).map(el -> {
                    log.info("Deleted " + el.getDeletedElement() + " elements");
                    return el;
                })
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER);

    }

}
