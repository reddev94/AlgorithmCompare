package com.reddev.algorithmcompare.core.domain.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteAlgorithmRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 9065928577115244278L;

    private String algorithm;
    private int[] array;

}
