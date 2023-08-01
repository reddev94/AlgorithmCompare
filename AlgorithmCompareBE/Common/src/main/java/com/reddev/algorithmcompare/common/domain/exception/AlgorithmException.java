package com.reddev.algorithmcompare.common.domain.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
public class AlgorithmException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2036965490007926119L;

    public AlgorithmException(int code, String description) {
        super(description);
        this.code = code;
        this.description = description;
    }

    private int code;
    private String description;

}
