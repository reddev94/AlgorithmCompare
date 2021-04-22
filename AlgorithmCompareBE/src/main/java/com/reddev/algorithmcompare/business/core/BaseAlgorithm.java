package com.reddev.algorithmcompare.business.core;

import com.reddev.algorithmcompare.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.model.AlgorithmDocument;
import com.reddev.algorithmcompare.model.AlgorithmException;
import com.reddev.algorithmcompare.model.BaseAlgorithmExecutionData;
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

    protected void saveOnDb(BaseAlgorithmExecutionData data) throws AlgorithmException {
        //calculate move execution time and save on db
        long actualTime = calculateTimestamp();
        long moveExecutionTime = actualTime - data.getInitialTime() + 1; //add 1 millisecond to avoid 0 as moveExecutionTime
        int saveResult = saveRecord(data.getIdRequester(), data.getArray(), data.getMoveOrder(), moveExecutionTime);
        if (saveResult != AlgorithmCompareUtil.RESULT_CODE_OK) {
            throw new AlgorithmException(saveResult, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_DB_ERROR);
        }
        //save MAX moveExecutionTime
        if (data.getMaxExecutionTime() < moveExecutionTime) {
            data.setMaxExecutionTime(moveExecutionTime);
        }
        data.setMoveOrder(data.getMoveOrder() + 1);
        data.setInitialTime(actualTime);
    }

    protected void firstSaveOnDb(BaseAlgorithmExecutionData data) throws AlgorithmException {
        int firstInsertResult = saveRecord(data.getIdRequester(), data.getArray(), data.getMoveOrder(), 0L);
        if (firstInsertResult != AlgorithmCompareUtil.RESULT_CODE_OK) {
            throw new AlgorithmException(firstInsertResult, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_DB_ERROR);
        }
        data.setMoveOrder(data.getMoveOrder() + 1);
    }

    private int saveRecord(String idRequester, int[] array, long moveOrder, long moveExecutionTime) {
        try {
            AlgorithmDocument document = new AlgorithmDocument();
            document.setIdRequester(idRequester);
            document.setArray(array);
            document.setMoveOrder(moveOrder);
            document.setMoveExecutionTime(moveExecutionTime);
            algorithmRepository.save(document).subscribeOn(AlgorithmCompareUtil.SCHEDULER).subscribe();
            //algorithmRepository.save(document).block();
            logger.debug("saved AlgorithmDocument = " + document.toString());
            return AlgorithmCompareUtil.RESULT_CODE_OK;
        } catch (Exception e) {
            logger.error("Exception during save record on db", e);
            return AlgorithmCompareUtil.RESULT_CODE_KO_DB_ERROR;
        }
    }
}
