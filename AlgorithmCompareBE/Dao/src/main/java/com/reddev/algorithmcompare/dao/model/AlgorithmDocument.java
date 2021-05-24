package com.reddev.algorithmcompare.dao.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Objects;

@Document
public class AlgorithmDocument {

    @Id
    private String id;
    private long idRequester;
    private int[] array;
    private long moveOrder;
    private long moveExecutionTime;
    private int indexOfSwappedElement;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getIdRequester() {
        return idRequester;
    }

    public void setIdRequester(long idRequester) {
        this.idRequester = idRequester;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public long getMoveOrder() {
        return moveOrder;
    }

    public void setMoveOrder(long moveOrder) {
        this.moveOrder = moveOrder;
    }

    public long getMoveExecutionTime() {
        return moveExecutionTime;
    }

    public void setMoveExecutionTime(long moveExecutionTime) {
        this.moveExecutionTime = moveExecutionTime;
    }

    public int getIndexOfSwappedElement() {
        return indexOfSwappedElement;
    }

    public void setIndexOfSwappedElement(int indexOfSwappedElement) {
        this.indexOfSwappedElement = indexOfSwappedElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlgorithmDocument that = (AlgorithmDocument) o;
        return moveOrder == that.moveOrder && moveExecutionTime == that.moveExecutionTime && indexOfSwappedElement == that.indexOfSwappedElement && Objects.equals(id, that.id) && Objects.equals(idRequester, that.idRequester) && Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, idRequester, moveOrder, moveExecutionTime, indexOfSwappedElement);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    @Override
    public String toString() {
        return "AlgorithmDocument{" +
                "idRequester='" + idRequester + '\'' +
                ", array=" + Arrays.toString(array) +
                ", moveOrder=" + moveOrder +
                ", moveExecutionTime=" + moveExecutionTime +
                ", indexOfSwappedElement=" + indexOfSwappedElement +
                '}';
    }
}
