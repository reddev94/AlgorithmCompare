package com.reddev.algorithmcompare.plugins.selectionsort;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.BaseAlgorithmExecutionData;
import com.reddev.algorithmcompare.plugins.pluginmodel.business.BaseAlgorithm;
import org.pf4j.Extension;

@Extension(ordinal = 1)
public class SelectionSortImpl extends BaseAlgorithm implements Algorithm {

    @Override
    public String getName() {
        return AlgorithmEnum.SELECTION_SORT.getValue();
    }

    @Override
    public long execute(int[] input, long idRequester) {

        BaseAlgorithmExecutionData data = BaseAlgorithmExecutionData.builder()
                .array(input)
                .idRequester(idRequester)
                .moveOrder(1L)
                .build();
        selectionSort(data);
        return data.getMaxExecutionTime();

    }

    private void selectionSort(BaseAlgorithmExecutionData data) {

        data.setInitialTime(calculateTimestamp());

        for (int i = 0; i < data.getArray().length - 1; i++) {
            int minIndex = i;

            // Find the index of the minimum element in the unsorted part of the array
            for (int j = i + 1; j < data.getArray().length; j++) {
                if (data.getArray()[j] < data.getArray()[minIndex]) {
                    minIndex = j;
                }
            }

            // Swap the found minimum element with the first element of the unsorted part
            if (minIndex != i) {
                // swap
                int temp = data.getArray()[i];
                data.getArray()[i] = data.getArray()[minIndex];
                data.getArray()[minIndex] = temp;
                data.setIndexOfSwappedElement(minIndex);
                saveOnDb(data);
            }

        }
    }

}
