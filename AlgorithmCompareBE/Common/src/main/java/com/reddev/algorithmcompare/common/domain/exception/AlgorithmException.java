package com.reddev.algorithmcompare.common.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class AlgorithmException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2036965490007926119L;

    private int code;
    private String description;

}
