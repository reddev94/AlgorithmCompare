package com.reddev.algorithmcompare.business.core.impl;

import com.reddev.algorithmcompare.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.business.core.Algorithm;
import com.reddev.algorithmcompare.business.core.BaseAlgorithm;
import com.reddev.algorithmcompare.model.AlgorithmEnum;
import com.reddev.algorithmcompare.model.AlgorithmException;
import org.springframework.stereotype.Component;

@Component
public class QuickSortImpl extends BaseAlgorithm implements Algorithm {
    @Override
    public String getName() {
        return AlgorithmEnum.QUICK_SORT.toString();
    }

    @Override
    public int execute(int[] input, long idRequester) throws AlgorithmException {
        int moveOrder = 0;
        int firstInsertResult = saveRecord(idRequester, input, moveOrder, 0);
        if (firstInsertResult != AlgorithmCompareUtil.RESULT_CODE_OK) {
            throw new AlgorithmException(firstInsertResult, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_DB_ERROR);
        }
        moveOrder++;
        quickSort(input, 0, input.length - 1, idRequester, moveOrder, 0L);
        return AlgorithmCompareUtil.RESULT_CODE_OK;
    }

    private void quickSort(int[] arr, int low, int high, long idRequester, long moveOrder, Long initialTime) throws AlgorithmException {
        if (initialTime == 0) {
            initialTime = calculateTimestamp();
        }
        if (low < high) {
            //partition the array around pi=>partitioning index and return pi
            int pi = partition(arr, low, high, idRequester, moveOrder, initialTime);
            //sort each partition recursively
            quickSort(arr, low, pi - 1, idRequester, moveOrder, initialTime);
            quickSort(arr, pi + 1, high, idRequester, moveOrder, initialTime);
        }
    }

    private int partition(int[] arr, int low, int high, long idRequester, long moveOrder, Long initialTime) throws AlgorithmException {
        int pi = arr[high];
        int i = (low - 1); // smaller element index
        for (int j = low; j < high; j++) {
            //check if current element is less than or equal to pi
            if (arr[j] <= pi) {
                i++;
                //swap intArray[i] and intArray[j]
                int temp = arr[i];
                arr[i] = arr[j];
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
        //swap intArray[i+1] and intArray[high] (or pi)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        //calculate move execution time and save on db
        long actualTime = calculateTimestamp();
        int saveResult = saveRecord(idRequester, arr, moveOrder, actualTime - initialTime);
        if (saveResult != AlgorithmCompareUtil.RESULT_CODE_OK) {
            throw new AlgorithmException(saveResult, AlgorithmCompareUtil.RESULT_DESCRIPTION_KO_DB_ERROR);
        }
        moveOrder++;
        initialTime = actualTime;
        return i + 1;
    }
}
