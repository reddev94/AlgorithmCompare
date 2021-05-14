package com.reddev.algorithmcompare.core.business.impl;

import com.reddev.algorithmcompare.commons.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.core.business.DbJobService;
import com.reddev.algorithmcompare.dao.AlgorithmCompareDAO;
import com.reddev.algorithmcompare.dao.model.AlgorithmDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DbJobServiceImpl implements DbJobService {
    private Logger logger = LoggerFactory.getLogger(DbJobServiceImpl.class);

    @Value("${job.seconds.old.data}")
    private long jobSecondsOldData;

    @Autowired
    private AlgorithmCompareDAO algorithmCompareDAO;

    @Override
    public Flux<Integer> deleteOldDbData() {
        long maxOldData = AlgorithmCompareUtil.getTimestampMinusSeconds(jobSecondsOldData);
        logger.info("maxOldData = " + maxOldData);
        return algorithmCompareDAO.deleteOldData(maxOldData)
                .reduce(0, (o1, o2) -> {
                    return o1++;
                }).concatWith(el -> logger.info("Deleted " + el + " old element"))
                .subscribeOn(AlgorithmCompareUtil.SCHEDULER);
    }
}
