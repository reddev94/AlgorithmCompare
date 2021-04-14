package com.reddev.algorithmcompare.business.core;

import com.reddev.algorithmcompare.model.AlgorithmException;

public interface Algorithm {

    public String getName();
    public int execute(int[] input, long idRequester) throws AlgorithmException;

}
