package com.reddev.algorithmcompare.core.domain.rest;

import com.reddev.algorithmcompare.common.domain.entity.SwappedElementInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetExecutionDataResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -5774344385140482148L;

    private int[] array;
    private long moveExecutionTime;
    private List<SwappedElementInfo> swappedElementInfo;
    private int executionStatus;

}
