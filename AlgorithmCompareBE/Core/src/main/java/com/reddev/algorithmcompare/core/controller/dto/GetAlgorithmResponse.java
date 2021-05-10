package com.reddev.algorithmcompare.core.controller.dto;

import java.util.List;
import java.util.Objects;

public class GetAlgorithmResponse extends BaseAlgorithmRestResponse {
    private List<String> availableAlgorithms;

    public List<String> getAvailableAlgorithms() {
        return availableAlgorithms;
    }

    public void setAvailableAlgorithms(List<String> availableAlgorithms) {
        this.availableAlgorithms = availableAlgorithms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GetAlgorithmResponse that = (GetAlgorithmResponse) o;
        return Objects.equals(availableAlgorithms, that.availableAlgorithms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), availableAlgorithms);
    }

    @Override
    public String toString() {
        return "GetAlgorithmResponse{" +
                "availableAlgorithms=" + availableAlgorithms +
                "} " + super.toString();
    }
}
