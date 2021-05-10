package com.reddev.algorithmcompare.core.business.core;

import com.reddev.algorithmcompare.core.model.AlgorithmException;

public interface Algorithm {

    public String getName();
    public long execute(int[] input, String idRequester) throws AlgorithmException;

}
