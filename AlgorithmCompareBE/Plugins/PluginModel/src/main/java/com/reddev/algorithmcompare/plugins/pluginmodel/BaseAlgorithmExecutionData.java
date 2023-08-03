package com.reddev.algorithmcompare.plugins.pluginmodel;

import com.reddev.algorithmcompare.common.domain.entity.SwappedElementInfo;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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
    private List<SwappedElementInfo> swappedElementInfo;

}
