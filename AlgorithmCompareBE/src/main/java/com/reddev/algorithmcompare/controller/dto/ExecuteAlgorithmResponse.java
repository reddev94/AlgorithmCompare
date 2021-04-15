package com.reddev.algorithmcompare.controller.dto;

import java.util.Objects;

public class ExecuteAlgorithmResponse extends BaseAlgorithmRestResponse {
    private String idRequester;

    public String getIdRequester() {
        return idRequester;
    }

    public void setIdRequester(String idRequester) {
        this.idRequester = idRequester;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExecuteAlgorithmResponse that = (ExecuteAlgorithmResponse) o;
        return Objects.equals(idRequester, that.idRequester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idRequester);
    }

    @Override
    public String toString() {
        return "ExecuteAlgorithmResponse{" +
                "idRequester='" + idRequester + '\'' +
                "} " + super.toString();
    }
}
