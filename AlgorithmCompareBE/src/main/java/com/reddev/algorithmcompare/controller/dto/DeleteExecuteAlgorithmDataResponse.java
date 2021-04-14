package com.reddev.algorithmcompare.controller.dto;

import java.util.Objects;

public class DeleteExecuteAlgorithmDataResponse extends BaseAlgorithmRestResponse {
    private int recordEliminated;

    public int getRecordEliminated() {
        return recordEliminated;
    }

    public void setRecordEliminated(int recordEliminated) {
        this.recordEliminated = recordEliminated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeleteExecuteAlgorithmDataResponse that = (DeleteExecuteAlgorithmDataResponse) o;
        return recordEliminated == that.recordEliminated;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), recordEliminated);
    }

    @Override
    public String toString() {
        return "DeleteExecuteAlgorithmDataResponse{" +
                "recordEliminated=" + recordEliminated +
                "} " + super.toString();
    }
}
