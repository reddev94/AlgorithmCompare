package com.reddev.algorithmcompare.common.domain.business;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum AlgorithmEnum {
    BUBBLE_SORT("BUBBLE SORT"),
    QUICK_SORT("QUICK SORT"),
    MERGE_SORT("MERGE SORT"),
    INSERTION_SORT("INSERTION SORT"),
    SELECTION_SORT("SELECTION SORT");

    private final String value;

    //****** Reverse Lookup Implementation************//
    //Lookup table
    private static final Map<String, AlgorithmEnum> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(AlgorithmEnum alg : AlgorithmEnum.values())
        {
            lookup.put(alg.getValue(), alg);
        }
    }

    //This method can be used for reverse lookup purpose
    public static AlgorithmEnum get(String value)
    {
        return lookup.get(value);
    }
}
