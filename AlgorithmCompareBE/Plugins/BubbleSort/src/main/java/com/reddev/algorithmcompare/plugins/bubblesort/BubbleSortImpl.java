package com.reddev.algorithmcompare.plugins.bubblesort;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
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
    public long execute(int[] input, long idRequester) {
        BaseAlgorithmExecutionData data = BaseAlgorithmExecutionData.builder()
                .array(input)
                .idRequester(idRequester)
                .moveOrder(1L)
                .build();
        bubbleSort(data);
        return data.getMaxExecutionTime();
    }

    private void bubbleSort(BaseAlgorithmExecutionData data) {
        data.setInitialTime(calculateTimestamp());
        int n = data.getArray().length;
        int temp;
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
