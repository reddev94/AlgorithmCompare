package com.reddev.algorithmcompare.model;

public class BaseAlgorithmExecutionData {
    private int[] array;
    private String idRequester;
    private long moveOrder;
    private long initialTime;

    public BaseAlgorithmExecutionData() {

    }

    public BaseAlgorithmExecutionData(int[] array, String idRequester) {
        this.array = array;
        this.idRequester = idRequester;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public String getIdRequester() {
        return idRequester;
    }

    public void setIdRequester(String idRequester) {
        this.idRequester = idRequester;
    }

    public long getMoveOrder() {
        return moveOrder;
    }

    public void setMoveOrder(long moveOrder) {
        this.moveOrder = moveOrder;
    }

    public long getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

}
