package com.reddev.algorithmcompare.controller.dto;

import java.io.Serializable;
import java.util.Objects;

public class BaseAlgorithmRestResponse implements Serializable {

    private int resultCode;
    private String resultDescription;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseAlgorithmRestResponse that = (BaseAlgorithmRestResponse) o;
        return resultCode == that.resultCode && Objects.equals(resultDescription, that.resultDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultCode, resultDescription);
    }

    @Override
    public String toString() {
        return "BaseAlgorithmRestResponse{" +
                "resultCode=" + resultCode +
                ", resultDescription='" + resultDescription + '\'' +
                '}';
    }
}
