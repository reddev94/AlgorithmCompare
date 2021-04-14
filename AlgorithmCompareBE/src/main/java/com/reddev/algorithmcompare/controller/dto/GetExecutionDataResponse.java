package com.reddev.algorithmcompare.controller.dto;

import java.util.Arrays;
import java.util.Objects;

public class GetExecutionDataResponse extends BaseAlgorithmRestResponse {
    private int[] array;
    private long moveExecutionTime;

    public GetExecutionDataResponse() {
    }

    public GetExecutionDataResponse(int[] array, long moveExecutionTime) {
        this.array = array;
        this.moveExecutionTime = moveExecutionTime;
    }

    public GetExecutionDataResponse(int[] array, long moveExecutionTime, int resultCode, String resultDescription) {
        this.array = array;
        this.moveExecutionTime = moveExecutionTime;
        setResultCode(resultCode);
        setResultDescription(resultDescription);
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public long getMoveExecutionTime() {
        return moveExecutionTime;
    }

    public void setMoveExecutionTime(long moveExecutionTime) {
        this.moveExecutionTime = moveExecutionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GetExecutionDataResponse that = (GetExecutionDataResponse) o;
        return moveExecutionTime == that.moveExecutionTime && Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), moveExecutionTime);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    @Override
    public String toString() {
        return "GetExecutionDataResponse{" +
                "array=" + Arrays.toString(array) +
                ", moveExecutionTime=" + moveExecutionTime +
                "} " + super.toString();
    }
}
