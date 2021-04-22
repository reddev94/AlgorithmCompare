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
    public long execute(int[] input, String idRequester) throws AlgorithmException {
        BaseAlgorithmExecutionData data = new BaseAlgorithmExecutionData(input, idRequester);
        firstSaveOnDb(data);
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
                    saveOnDb(data);
                }
            }
        }
    }
}
