package com.reddev.algorithmcompare.business.core.impl;

import com.reddev.algorithmcompare.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.business.core.Algorithm;
import com.reddev.algorithmcompare.business.core.BaseAlgorithm;
import com.reddev.algorithmcompare.model.AlgorithmEnum;
import com.reddev.algorithmcompare.model.AlgorithmException;
import com.reddev.algorithmcompare.model.BaseAlgorithmExecutionData;
import com.reddev.algorithmcompare.model.QuickSortAlgorithmExecutionData;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class QuickSortImpl extends BaseAlgorithm implements Algorithm {
    @Override
    public String getName() {
        return AlgorithmEnum.QUICK_SORT.toString();
    }

    @Override
    public int execute(int[] input, String idRequester) throws AlgorithmException {
        BaseAlgorithmExecutionData data = new BaseAlgorithmExecutionData(input, idRequester);
        int firstInsertResult = saveRecord(data.getIdRequester(), input, data.getMoveOrder(), 0L);
        if (firstInsertResult != AlgorithmCompareUtil.RESULT_CODE_OK) {
            throw new AlgorithmException(firstInsertResult, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_DB_ERROR);
        }
        data.setMoveOrder(data.getMoveOrder() + 1);
        //data.setHigh(input.length - 1);
        quickSort(input, data, 0, data.getArray().length-1);
        return AlgorithmCompareUtil.RESULT_CODE_OK;
    }

    private void quickSort(int[] input, BaseAlgorithmExecutionData data, int low, int high) throws AlgorithmException {
        if (data.getInitialTime() == 0L) {
            data.setInitialTime(calculateTimestamp());
        }
        if (low < high) {
            //partition the array around pi=>partitioning index and return pi
            int pi = partition(input, data, low, high);
            //sort each partition recursively
            //data.setHigh(pi);
            quickSort(input, data, low, pi-1);
            quickSort(input, data, pi+1, high);
        }
    }

    private int partition(int[] input, BaseAlgorithmExecutionData data, int low, int high) throws AlgorithmException {
        int pi = input[high];
        int i = (low - 1); // smaller element index
        for (int j = low; j < high; j++) {
            //check if current element is less than or equal to pi
            if (input[j] <= pi) {
                i++;
                //swap intArray[i] and intArray[j]
                int temp = input[i];
                input[i] = input[j];
                input[j] = temp;
                System.out.println("array="+ Arrays.toString(input));
                //calculate move execution time and save on db
                long actualTime = calculateTimestamp();
                int saveResult = saveRecord(data.getIdRequester(), input, data.getMoveOrder(), actualTime - data.getInitialTime());
                if (saveResult != AlgorithmCompareUtil.RESULT_CODE_OK) {
                    throw new AlgorithmException(saveResult, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_DB_ERROR);
                }
                data.setMoveOrder(data.getMoveOrder() + 1);
                data.setInitialTime(actualTime);
            }
        }
        //swap intArray[i+1] and intArray[high] (or pi)
        int temp = input[i + 1];
        input[i + 1] = input[high];
        input[high] = temp;
        System.out.println("array="+ Arrays.toString(input));
        //calculate move execution time and save on db
        long actualTime = calculateTimestamp();
        int saveResult = saveRecord(data.getIdRequester(), input, data.getMoveOrder(), actualTime - data.getInitialTime());
        if (saveResult != AlgorithmCompareUtil.RESULT_CODE_OK) {
            throw new AlgorithmException(saveResult, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_DB_ERROR);
        }
        data.setMoveOrder(data.getMoveOrder() + 1);
        data.setInitialTime(actualTime);
        return i + 1;
    }
}
