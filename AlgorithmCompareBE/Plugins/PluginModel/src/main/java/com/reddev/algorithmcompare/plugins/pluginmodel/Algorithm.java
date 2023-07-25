package com.reddev.algorithmcompare.plugins.pluginmodel;

import org.pf4j.ExtensionPoint;

public interface Algorithm extends ExtensionPoint {
    String getName();
    long execute(int[] input, long idRequester);
}
