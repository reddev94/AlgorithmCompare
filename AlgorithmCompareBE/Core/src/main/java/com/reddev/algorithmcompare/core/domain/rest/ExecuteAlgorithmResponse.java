package com.reddev.algorithmcompare.core.domain.rest;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class ExecuteAlgorithmResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -5915649053210436726L;

    private long idRequester;
    private long maxExecutionTime;

}
