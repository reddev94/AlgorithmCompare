package com.reddev.algorithmcompare.core.domain.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateArrayResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2345629232798670259L;

    private int[] array;

}
