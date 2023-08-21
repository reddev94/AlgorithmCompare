package com.reddev.algorithmcompare.plugins.selectionsort;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.BaseAlgorithmExecutionData;
import com.reddev.algorithmcompare.plugins.pluginmodel.business.BaseAlgorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.business.StringToColor;
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
                .array(convertToInfoArray(input))
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
                if (data.getArray()[j].value < data.getArray()[minIndex].value) {
                    minIndex = j;
                }
            }

            // Swap the found minimum element with the first element of the unsorted part
            if (minIndex != i) {
                // save before swap
                saveInfo(data, new int[]{i, minIndex}, new String[]{StringToColor.RED.getValue(), StringToColor.BLUE.getValue()});
                // swap
                int temp = data.getArray()[i].value;
                data.getArray()[i].value = data.getArray()[minIndex].value;
                data.getArray()[minIndex].value = temp;
                // save after swap
                saveInfo(data, new int[]{i, minIndex}, new String[]{StringToColor.BLUE.getValue(), StringToColor.RED.getValue()});
            }

        }
    }

}
