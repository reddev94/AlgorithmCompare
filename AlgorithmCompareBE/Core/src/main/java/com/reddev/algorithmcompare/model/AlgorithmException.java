package com.reddev.algorithmcompare.model;

import java.util.Objects;

public class AlgorithmException extends Exception {

    private int code;
    private String description;

    public AlgorithmException(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlgorithmException that = (AlgorithmException) o;
        return code == that.code && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, description);
    }

    @Override
    public String toString() {
        return "AlgorithmException{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
