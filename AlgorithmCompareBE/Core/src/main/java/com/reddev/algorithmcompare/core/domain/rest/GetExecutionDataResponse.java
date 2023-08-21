package com.reddev.algorithmcompare.core.domain.rest;

import com.reddev.algorithmcompare.common.domain.entity.ArrayInfo;
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
public class GetExecutionDataResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -5774344385140482148L;

    private ArrayInfo[] array;
    private long moveExecutionTime;
    private int executionStatus;

}
