package com.reddev.algorithmcompare.plugins.pluginmodel;

import com.reddev.algorithmcompare.commons.model.AlgorithmException;
import org.pf4j.ExtensionPoint;

public interface Algorithm extends ExtensionPoint {
    public String getName();
    public long execute(int[] input, long idRequester) throws AlgorithmException;
}
