package com.reddev.algorithmcompare.core.domain.rest;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class ExecuteAlgorithmRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 9065928577115244278L;

    private String algorithm;
    private int[] array;

}
