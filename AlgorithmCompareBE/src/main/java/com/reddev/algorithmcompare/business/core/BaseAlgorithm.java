package com.reddev.algorithmcompare.business.core;

import com.reddev.algorithmcompare.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.model.AlgorithmDocument;
import com.reddev.algorithmcompare.repository.AlgorithmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class BaseAlgorithm {
    protected Logger logger = LoggerFactory.getLogger(BaseAlgorithm.class);

    @Autowired
    protected AlgorithmRepository algorithmRepository;

    protected long calculateTimestamp() {
        return AlgorithmCompareUtil.getTimestamp();
    }

    protected int saveRecord(String idRequester, int[] array, long moveOrder, long moveExecutionTime) {
        try {
            AlgorithmDocument document = new AlgorithmDocument();
            document.setIdRequester(idRequester);
            document.setArray(array);
            document.setMoveOrder(moveOrder);
            document.setMoveExecutionTime(moveExecutionTime);
            //algorithmRepository.save(document).subscribeOn(AlgorithmCompareUtil.SCHEDULER).subscribe();
            algorithmRepository.save(document).block();
            logger.debug("saved AlgorithmDocument = " + document.toString());
            return AlgorithmCompareUtil.RESULT_CODE_OK;
        } catch (Exception e) {
            logger.error("Exception during save record on db", e);
            return AlgorithmCompareUtil.RESULT_CODE_KO_DB_ERROR;
        }
    }
}
