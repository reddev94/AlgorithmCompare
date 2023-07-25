package com.reddev.algorithmcompare.core.domain.rest;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
public class ErrorResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4398744237773302307L;

    private int status;
    private String path;
    private Instant timestamp;
    private String message;
    private int error;
    private String exception;

}
