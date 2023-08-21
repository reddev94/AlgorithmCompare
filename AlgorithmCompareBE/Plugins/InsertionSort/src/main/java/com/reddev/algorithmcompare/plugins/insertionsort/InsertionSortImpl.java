package com.reddev.algorithmcompare.plugins.insertionsort;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.BaseAlgorithmExecutionData;
import com.reddev.algorithmcompare.plugins.pluginmodel.business.BaseAlgorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.business.StringToColor;
import org.pf4j.Extension;

@Extension(ordinal = 1)
public class InsertionSortImpl extends BaseAlgorithm implements Algorithm {

    @Override
    public String getName() {
        return AlgorithmEnum.INSERTION_SORT.getValue();
    }

    @Override
    public long execute(int[] input, long idRequester) {

        BaseAlgorithmExecutionData data = BaseAlgorithmExecutionData.builder()
                .array(convertToInfoArray(input))
                .idRequester(idRequester)
                .moveOrder(1L)
                .build();
        insertionSort(data);
        return data.getMaxExecutionTime();

    }

    private void insertionSort(BaseAlgorithmExecutionData data) {

        data.setInitialTime(calculateTimestamp());

        for (int i = 1; i < data.getArray().length; i++) {
            int key = data.getArray()[i].value;
            int j = i - 1;

            // Shift elements greater than the key to the right
            while (j >= 0 && data.getArray()[j].value > key) {
                // shift
                data.getArray()[j + 1].value = data.getArray()[j].value;
                // save after shift
                saveInfo(data, new int[]{j + 1, i}, new String[]{StringToColor.RED.getValue(), StringToColor.YELLOW.getValue()});
                j--;
            }

            // Insert the key into the correct position
            data.getArray()[j + 1].value = key;
            // save after key insertion
            saveInfo(data, new int[]{i, j + 1}, new String[]{StringToColor.YELLOW.getValue(), StringToColor.RED.getValue()});
        }

    }

}
