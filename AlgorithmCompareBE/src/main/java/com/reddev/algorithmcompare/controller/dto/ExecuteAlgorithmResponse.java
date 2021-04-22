package com.reddev.algorithmcompare.controller.dto;

import java.util.Objects;

public class ExecuteAlgorithmResponse extends BaseAlgorithmRestResponse {
    private String idRequester;
    private long maxExecutionTime;

    public String getIdRequester() {
        return idRequester;
    }

    public void setIdRequester(String idRequester) {
        this.idRequester = idRequester;
    }

    public long getMaxExecutionTime() {
        return maxExecutionTime;
    }

    public void setMaxExecutionTime(long maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExecuteAlgorithmResponse that = (ExecuteAlgorithmResponse) o;
        return maxExecutionTime == that.maxExecutionTime && Objects.equals(idRequester, that.idRequester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idRequester, maxExecutionTime);
    }

    @Override
    public String toString() {
        return "ExecuteAlgorithmResponse{" +
                "idRequester='" + idRequester + '\'' +
                ", maxExecutionTime=" + maxExecutionTime +
                "} " + super.toString();
    }
}
