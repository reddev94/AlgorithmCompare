package com.reddev.algorithmcompare.business.core.impl;

import com.reddev.algorithmcompare.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.business.core.Algorithm;
import com.reddev.algorithmcompare.business.core.BaseAlgorithm;
import com.reddev.algorithmcompare.model.AlgorithmEnum;
import com.reddev.algorithmcompare.model.AlgorithmException;
import com.reddev.algorithmcompare.model.BaseAlgorithmExecutionData;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BubbleSortImpl extends BaseAlgorithm implements Algorithm {
    @Override
    public String getName() {
        return AlgorithmEnum.BUBBLE_SORT.toString();
    }

    @Override
    public int execute(int[] input, String idRequester) throws AlgorithmException {
        BaseAlgorithmExecutionData data = new BaseAlgorithmExecutionData(input, idRequester);
        int firstInsertResult = saveRecord(data.getIdRequester(), data.getArray(), data.getMoveOrder(), 0L);
        if (firstInsertResult != AlgorithmCompareUtil.RESULT_CODE_OK) {
            throw new AlgorithmException(firstInsertResult, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_DB_ERROR);
        }
        data.setMoveOrder(data.getMoveOrder() + 1);
        bubbleSort(data);
        return AlgorithmCompareUtil.RESULT_CODE_OK;
    }

    private void bubbleSort(BaseAlgorithmExecutionData data) throws AlgorithmException {
        data.setInitialTime(calculateTimestamp());
        int n = data.getArray().length;
        int temp = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (data.getArray()[j - 1] > data.getArray()[j]) {
                    //swap elements
                    temp = data.getArray()[j - 1];
                    data.getArray()[j - 1] = data.getArray()[j];
                    data.getArray()[j] = temp;
                    //calculate move execution time and save on db
                    long actualTime = calculateTimestamp();
                    int saveResult = saveRecord(data.getIdRequester(), data.getArray(), data.getMoveOrder(), actualTime - data.getInitialTime());
                    if (saveResult != AlgorithmCompareUtil.RESULT_CODE_OK) {
                        throw new AlgorithmException(saveResult, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_DB_ERROR);
                    }
                    data.setMoveOrder(data.getMoveOrder() + 1);
                    data.setInitialTime(actualTime);
                }
            }
        }
    }
}
