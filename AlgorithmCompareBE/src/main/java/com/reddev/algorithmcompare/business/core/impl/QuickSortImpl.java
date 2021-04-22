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
public class QuickSortImpl extends BaseAlgorithm implements Algorithm {
    @Override
    public String getName() {
        return AlgorithmEnum.QUICK_SORT.toString();
    }

    @Override
    public long execute(int[] input, String idRequester) throws AlgorithmException {
        BaseAlgorithmExecutionData data = new BaseAlgorithmExecutionData(input, idRequester);
        firstSaveOnDb(data);
        quickSort(data, 0, data.getArray().length - 1);
        return data.getMaxExecutionTime();
    }

    private void quickSort(BaseAlgorithmExecutionData data, int low, int high) throws AlgorithmException {
        if (data.getInitialTime() == 0L) {
            data.setInitialTime(calculateTimestamp());
        }
        if (low < high) {
            //partition the array around pi=>partitioning index and return pi
            int pi = partition(data, low, high);
            //sort each partition recursively
            quickSort(data, low, pi - 1);
            quickSort(data, pi + 1, high);
        }
    }

    private int partition(BaseAlgorithmExecutionData data, int low, int high) throws AlgorithmException {
        int pi = data.getArray()[high];
        int i = (low - 1); // smaller element index
        for (int j = low; j < high; j++) {
            //check if current element is less than or equal to pi
            if (data.getArray()[j] <= pi) {
                i++;
                if (i != j) {
                    //swap intArray[i] and intArray[j]
                    int temp = data.getArray()[i];
                    data.getArray()[i] = data.getArray()[j];
                    data.getArray()[j] = temp;
                    saveOnDb(data);
                }
            }
        }
        if ((i + 1) != high) {
            //swap intArray[i+1] and intArray[high] (or pi)
            int temp = data.getArray()[i + 1];
            data.getArray()[i + 1] = data.getArray()[high];
            data.getArray()[high] = temp;
            saveOnDb(data);
        }
        return i + 1;
    }
}
