package com.reddev.algorithmcompare.plugins.pluginmodel;

public class BaseAlgorithmExecutionData {
    private int[] array;
    private long idRequester;
    private long moveOrder;
    private long initialTime;
    private long maxExecutionTime;
    private int indexOfSwappedElement;

    public BaseAlgorithmExecutionData() {

    }

    public BaseAlgorithmExecutionData(int[] array, long idRequester) {
        this.array = array;
        this.idRequester = idRequester;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public long getIdRequester() {
        return idRequester;
    }

    public void setIdRequester(long idRequester) {
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

    public long getMaxExecutionTime() {
        return maxExecutionTime;
    }

    public void setMaxExecutionTime(long maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }

    public int getIndexOfSwappedElement() {
        return indexOfSwappedElement;
    }

    public void setIndexOfSwappedElement(int indexOfSwappedElement) {
        this.indexOfSwappedElement = indexOfSwappedElement;
    }
}
