package com.reddev.algorithmcompare.plugins.mergesort;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.BaseAlgorithmExecutionData;
import com.reddev.algorithmcompare.plugins.pluginmodel.business.BaseAlgorithm;
import org.pf4j.Extension;

@Extension(ordinal = 1)
public class MergeSortImpl extends BaseAlgorithm implements Algorithm {

    @Override
    public String getName() {
        return AlgorithmEnum.MERGE_SORT.getValue();
    }

    @Override
    public long execute(int[] input, long idRequester) {

        BaseAlgorithmExecutionData data = BaseAlgorithmExecutionData.builder()
                .array(input)
                .idRequester(idRequester)
                .moveOrder(1L)
                .build();
        mergeSort(data);
        return data.getMaxExecutionTime();

    }

    private void mergeSort(BaseAlgorithmExecutionData data) {

        int[] tempArray = new int[data.getArray().length];
        data.setInitialTime(calculateTimestamp());
        mergeSort(data, tempArray, 0, data.getArray().length - 1);

    }

    private void mergeSort(BaseAlgorithmExecutionData data, int[] tempArray, int left, int right) {

        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(data, tempArray, left, mid); // Sort the left half
            mergeSort(data, tempArray, mid + 1, right); // Sort the right half
            merge(data, tempArray, left, mid, right); // Merge the two sorted halves
        }

    }

    private void merge(BaseAlgorithmExecutionData data, int[] tempArray, int left, int mid, int right) {

        // Copy elements to the temporary array
        if (right + 1 - left >= 0) System.arraycopy(data.getArray(), left, tempArray, left, right + 1 - left);

        int i = left; // Pointer for the left half
        int j = mid + 1; // Pointer for the right half
        int k = left; // Pointer for the merged array

        while (i <= mid && j <= right) {
            if (tempArray[i] <= tempArray[j]) {
                // swap
                data.getArray()[k] = tempArray[i];
                data.setIndexOfSwappedElement(k);
                saveOnDb(data);
                i++;
            } else {
                // swap
                data.getArray()[k] = tempArray[j];
                data.setIndexOfSwappedElement(k);
                saveOnDb(data);
                j++;
            }
            k++;
        }

        // Copy the remaining elements from the left half (if any)
        while (i <= mid) {
            // swap
            data.getArray()[k] = tempArray[i];
            data.setIndexOfSwappedElement(k);
            saveOnDb(data);
            i++;
            k++;
        }

        // Copy the remaining elements from the right half (if any)
        while (j <= right) {
            // swap
            data.getArray()[k] = tempArray[j];
            data.setIndexOfSwappedElement(k);
            saveOnDb(data);
            j++;
            k++;
        }

    }

}