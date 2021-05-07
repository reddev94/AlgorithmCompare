package com.reddev.algorithmcompare.business.core;

import com.reddev.algorithmcompare.model.AlgorithmException;

public interface Algorithm {

    public String getName();
    public long execute(int[] input, String idRequester) throws AlgorithmException;

}
