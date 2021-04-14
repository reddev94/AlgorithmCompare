package com.reddev.algorithmcompare.controller.dto;

import java.util.Arrays;

public class GenerateArrayResponse extends BaseAlgorithmRestResponse {
    private int[] array;

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
        if (!super.equals(o)) return false;
        GenerateArrayResponse that = (GenerateArrayResponse) o;
        return Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    @Override
    public String toString() {
        return "GenerateArrayResponse{" +
                "array=" + Arrays.toString(array) +
                "} " + super.toString();
    }
}
