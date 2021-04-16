package com.reddev.algorithmcompare.model;

public class QuickSortAlgorithmExecutionData extends BaseAlgorithmExecutionData {
    private int low;
    private int high;

    public QuickSortAlgorithmExecutionData() {
        super();
    }

    public QuickSortAlgorithmExecutionData(int[] array, String idRequester) {
        super(array, idRequester);
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }
}
