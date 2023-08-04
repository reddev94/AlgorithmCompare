package com.reddev.algorithmcompare.plugins.insertionsort;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.BaseAlgorithmExecutionData;
import com.reddev.algorithmcompare.plugins.pluginmodel.business.BaseAlgorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.business.StringToColor;
import org.pf4j.Extension;

import java.util.List;

@Extension(ordinal = 1)
public class InsertionSortImpl extends BaseAlgorithm implements Algorithm {

    @Override
    public String getName() {
        return AlgorithmEnum.INSERTION_SORT.getValue();
    }

    @Override
    public long execute(int[] input, long idRequester) {

        BaseAlgorithmExecutionData data = BaseAlgorithmExecutionData.builder()
                .array(input)
                .idRequester(idRequester)
                .moveOrder(1L)
                .build();
        insertionSort(data);
        return data.getMaxExecutionTime();

    }

    private void insertionSort(BaseAlgorithmExecutionData data) {

        data.setInitialTime(calculateTimestamp());

        for (int i = 1; i < data.getArray().length; i++) {
            int key = data.getArray()[i];
            int j = i - 1;

            // Shift elements greater than the key to the right
            while (j >= 0 && data.getArray()[j] > key) {
                // shift
                data.getArray()[j + 1] = data.getArray()[j];
                // save after shift
                saveInfo(data, List.of(j + 1, i), List.of(StringToColor.RED.getValue(), StringToColor.YELLOW.getValue()));
                j--;
            }

            // Insert the key into the correct position
            data.getArray()[j + 1] = key;
            // save after key insertion
            saveInfo(data, List.of(i, j + 1), List.of(StringToColor.YELLOW.getValue(), StringToColor.RED.getValue()));
        }

    }

}
