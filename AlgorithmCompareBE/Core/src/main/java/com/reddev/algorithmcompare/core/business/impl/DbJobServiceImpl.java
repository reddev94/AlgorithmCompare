package com.reddev.algorithmcompare.core.business.impl;

import com.reddev.algorithmcompare.commons.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.core.business.DbJobService;
import com.reddev.algorithmcompare.core.business.DeleteOldDataJobResponse;
import com.reddev.algorithmcompare.dao.AlgorithmCompareDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DbJobServiceImpl implements DbJobService {
    private Logger logger = LoggerFactory.getLogger(DbJobServiceImpl.class);

    @Value("${job.seconds.old.data}")
    private long jobSecondsOldData;

    @Autowired
    private AlgorithmCompareDAO algorithmCompareDAO;

    @Override
    public Mono<DeleteOldDataJobResponse> deleteOldDbData() {
        DeleteOldDataJobResponse response = new DeleteOldDataJobResponse();
        long maxOldData = AlgorithmCompareUtil.getTimestampMinusSeconds(jobSecondsOldData);
        logger.info("maxOldData = " + maxOldData);
        return algorithmCompareDAO.deleteOldData(maxOldData)
                .reduce(response, (o1, o2) -> {
                    response.setDeletedElement(1 + o1.getDeletedElement());
                    return response;
                }).map(el -> {
                    logger.info("Deleted " + el.getDeletedElement() + " elements");
                    return el;
                })
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER);
    }

}
