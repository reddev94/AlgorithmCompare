package com.reddev.algorithmcompare.core.domain.rest;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class GetExecutionDataResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -5774344385140482148L;

    private int[] array;
    private long moveExecutionTime;
    private int indexOfSwappedElement;
    private int executionStatus;

}
