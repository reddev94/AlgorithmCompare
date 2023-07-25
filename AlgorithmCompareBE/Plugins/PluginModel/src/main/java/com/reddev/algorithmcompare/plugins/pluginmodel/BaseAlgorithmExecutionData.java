package com.reddev.algorithmcompare.plugins.pluginmodel;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class BaseAlgorithmExecutionData implements Serializable {

    @Serial
    private static final long serialVersionUID = 6870247015734620197L;

    private int[] array;
    private long idRequester;
    private long moveOrder;
    private long initialTime;
    private long maxExecutionTime;
    private int indexOfSwappedElement;

}
