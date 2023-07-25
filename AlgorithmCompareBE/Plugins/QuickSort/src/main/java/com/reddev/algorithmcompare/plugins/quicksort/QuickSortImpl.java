package com.reddev.algorithmcompare.plugins.quicksort;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.BaseAlgorithmExecutionData;
import com.reddev.algorithmcompare.plugins.pluginmodel.business.BaseAlgorithm;
import org.pf4j.Extension;

@Extension(ordinal = 1)
public class QuickSortImpl extends BaseAlgorithm implements Algorithm {

    @Override
    public String getName() {
        return AlgorithmEnum.QUICK_SORT.getValue();
    }

    @Override
    public long execute(int[] input, long idRequester) {
        BaseAlgorithmExecutionData data = BaseAlgorithmExecutionData.builder()
                .array(input)
                .idRequester(idRequester)
                .moveOrder(1L)
                .build();
        quickSort(data, 0, data.getArray().length - 1);
        return data.getMaxExecutionTime();
    }

    private void quickSort(BaseAlgorithmExecutionData data, int low, int high) {
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

    private int partition(BaseAlgorithmExecutionData data, int low, int high) {
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
                    data.setIndexOfSwappedElement(j);
                    saveOnDb(data);
                }
            }
        }
        if ((i + 1) != high) {
            //swap intArray[i+1] and intArray[high] (or pi)
            int temp = data.getArray()[i + 1];
            data.getArray()[i + 1] = data.getArray()[high];
            data.getArray()[high] = temp;
            data.setIndexOfSwappedElement(high);
            saveOnDb(data);
        }
        return i + 1;
    }
}
