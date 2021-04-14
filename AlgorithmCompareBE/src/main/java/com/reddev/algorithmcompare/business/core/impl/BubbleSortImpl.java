package com.reddev.algorithmcompare.business.core.impl;

import com.reddev.algorithmcompare.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.business.core.Algorithm;
import com.reddev.algorithmcompare.business.core.BaseAlgorithm;
import com.reddev.algorithmcompare.model.AlgorithmEnum;
import com.reddev.algorithmcompare.model.AlgorithmException;
import org.springframework.stereotype.Component;

@Component
public class BubbleSortImpl extends BaseAlgorithm implements Algorithm {
    @Override
    public String getName() {
        return AlgorithmEnum.BUBBLE_SORT.toString();
    }

    @Override
    public int execute(int[] input, long idRequester) throws AlgorithmException {
        int moveOrder = 0;
        int firstInsertResult = saveRecord(idRequester, input, moveOrder, 0);
        if (firstInsertResult != AlgorithmCompareUtil.RESULT_CODE_OK) {
            throw new AlgorithmException(firstInsertResult, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_DB_ERROR);
        }
        moveOrder++;
        bubbleSort(input, idRequester, moveOrder);
        return AlgorithmCompareUtil.RESULT_CODE_OK;
    }

    private void bubbleSort(int[] arr, long idRequester, long moveOrder) throws AlgorithmException {
        long initialTime = calculateTimestamp();
        int n = arr.length;
        int temp = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (arr[j - 1] > arr[j]) {
                    //swap elements
                    temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                    //calculate move execution time and save on db
                    long actualTime = calculateTimestamp();
                    int saveResult = saveRecord(idRequester, arr, moveOrder, actualTime - initialTime);
                    if (saveResult != AlgorithmCompareUtil.RESULT_CODE_OK) {
                        throw new AlgorithmException(saveResult, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_DB_ERROR);
                    }
                    moveOrder++;
                    initialTime = actualTime;
                }
            }
        }
    }
}
