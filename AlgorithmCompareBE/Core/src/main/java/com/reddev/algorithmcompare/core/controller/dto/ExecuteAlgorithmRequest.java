package com.reddev.algorithmcompare.core.controller.dto;

import com.reddev.algorithmcompare.commons.model.AlgorithmEnum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class ExecuteAlgorithmRequest implements Serializable {

    private AlgorithmEnum algorithm;
    private int[] array;

    public AlgorithmEnum getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AlgorithmEnum algorithm) {
        this.algorithm = algorithm;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecuteAlgorithmRequest that = (ExecuteAlgorithmRequest) o;
        return algorithm == that.algorithm && Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(algorithm);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    @Override
    public String toString() {
        return "ExecuteAlgorithmRequest{" +
                "algorithm=" + algorithm +
                ", array=" + Arrays.toString(array) +
                '}';
    }
}
