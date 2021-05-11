package com.reddev.algorithmcompare.plugins.pluginmodel.business;

import com.reddev.algorithmcompare.commons.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.commons.model.AlgorithmException;
import com.reddev.algorithmcompare.dao.AlgorithmCompareDAO;
import com.reddev.algorithmcompare.plugins.pluginmodel.BaseAlgorithmExecutionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseAlgorithm {
    protected Logger logger = LoggerFactory.getLogger(BaseAlgorithm.class);

    @Autowired
    private AlgorithmCompareDAO algorithmCompareDAO;

    protected long calculateTimestamp() {
        return AlgorithmCompareUtil.getTimestamp();
    }

    protected void saveOnDb(BaseAlgorithmExecutionData data) throws AlgorithmException {
        //calculate move execution time and save on db
        long actualTime = calculateTimestamp();
        long moveExecutionTime = (actualTime - data.getInitialTime()) + 1L; //add 1 millisecond to avoid 0 as moveExecutionTime
        int saveResult = saveRecord(data.getIdRequester(), data.getArray(), data.getMoveOrder(), moveExecutionTime, data.getIndexOfSwappedElement());
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

    private int saveRecord(String idRequester, int[] array, long moveOrder, long moveExecutionTime, int indexOfSwappedElement) {
        try {
            algorithmCompareDAO.saveDocument(array, idRequester, moveExecutionTime, moveOrder, indexOfSwappedElement).subscribe();
            return AlgorithmCompareUtil.RESULT_CODE_OK;
        } catch (Exception e) {
            logger.error("Exception during save record on db", e);
            return AlgorithmCompareUtil.RESULT_CODE_KO_DB_ERROR;
        }
    }
}
