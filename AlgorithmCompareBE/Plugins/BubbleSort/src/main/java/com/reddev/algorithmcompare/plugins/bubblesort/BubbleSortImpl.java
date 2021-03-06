package com.reddev.algorithmcompare.plugins.bubblesort;

import com.reddev.algorithmcompare.commons.model.AlgorithmEnum;
import com.reddev.algorithmcompare.commons.model.AlgorithmException;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.BaseAlgorithmExecutionData;
import com.reddev.algorithmcompare.plugins.pluginmodel.business.BaseAlgorithm;
import org.pf4j.Extension;

@Extension(ordinal = 1)
public class BubbleSortImpl extends BaseAlgorithm implements Algorithm {
    @Override
    public String getName() {
        return AlgorithmEnum.BUBBLE_SORT.getValue();
    }

    @Override
    public long execute(int[] input, long idRequester) throws AlgorithmException {
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
