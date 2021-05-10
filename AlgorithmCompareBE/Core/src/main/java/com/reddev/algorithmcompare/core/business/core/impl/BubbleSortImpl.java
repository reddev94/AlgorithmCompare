package com.reddev.algorithmcompare.core.business.core.impl;

import com.reddev.algorithmcompare.core.business.core.Algorithm;
import com.reddev.algorithmcompare.core.business.core.BaseAlgorithm;
import com.reddev.algorithmcompare.core.model.AlgorithmEnum;
import com.reddev.algorithmcompare.core.model.AlgorithmException;
import com.reddev.algorithmcompare.core.model.BaseAlgorithmExecutionData;
import org.springframework.stereotype.Component;

@Component
public class BubbleSortImpl extends BaseAlgorithm implements Algorithm {
    @Override
    public String getName() {
        return AlgorithmEnum.BUBBLE_SORT.toString();
    }

    @Override
    public long execute(int[] input, String idRequester) throws AlgorithmException {
        BaseAlgorithmExecutionData data = new BaseAlgorithmExecutionData(input, idRequester);
        data.setMoveOrder(1L);
        bubbleSort(data);
        return data.getMaxExecutionTime();
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
                    data.setIndexOfSwappedElement(j);
                    saveOnDb(data);
                }
            }
        }
    }
}
