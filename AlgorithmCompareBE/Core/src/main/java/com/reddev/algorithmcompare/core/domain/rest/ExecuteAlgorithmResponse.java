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
public class ExecuteAlgorithmResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -5915649053210436726L;

    private long idRequester;
    private long maxExecutionTime;

}
