package com.reddev.algorithmcompare.controller.dto;

import java.util.Objects;

public class GetMaxExecutionTimeResponse extends BaseAlgorithmRestResponse {
    private long maxExecutionTime;

    public GetMaxExecutionTimeResponse() {

    }

    public GetMaxExecutionTimeResponse(long maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }

    public GetMaxExecutionTimeResponse(long maxExecutionTime, int resultCode, String resultDescription) {
        this.maxExecutionTime = maxExecutionTime;
        setResultCode(resultCode);
        setResultDescription(resultDescription);
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
        GetMaxExecutionTimeResponse that = (GetMaxExecutionTimeResponse) o;
        return maxExecutionTime == that.maxExecutionTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), maxExecutionTime);
    }

    @Override
    public String toString() {
        return "GetMaxExecutionTimeResponse{" +
                "maxExecutionTime=" + maxExecutionTime +
                "} " + super.toString();
    }
}
