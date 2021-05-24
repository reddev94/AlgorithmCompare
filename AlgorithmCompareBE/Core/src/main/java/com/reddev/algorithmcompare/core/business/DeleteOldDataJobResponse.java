package com.reddev.algorithmcompare.core.business;

import java.util.Objects;

public class DeleteOldDataJobResponse {
    private int deletedElement;

    public int getDeletedElement() {
        return deletedElement;
    }

    public void setDeletedElement(int deletedElement) {
        this.deletedElement = deletedElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteOldDataJobResponse that = (DeleteOldDataJobResponse) o;
        return deletedElement == that.deletedElement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deletedElement);
    }

    @Override
    public String toString() {
        return "DeleteOldDataJobResponse{" +
                "deletedElement=" + deletedElement +
                '}';
    }
}
