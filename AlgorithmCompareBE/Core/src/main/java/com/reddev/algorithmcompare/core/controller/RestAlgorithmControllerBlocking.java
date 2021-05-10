package com.reddev.algorithmcompare.core.controller;


import com.reddev.algorithmcompare.core.controller.dto.GenerateArrayResponse;
import com.reddev.algorithmcompare.core.controller.dto.GetAlgorithmResponse;

public interface RestAlgorithmControllerBlocking {
    public GenerateArrayResponse generateArray(int length);
    public GetAlgorithmResponse getAvailableAlgorithms();
}
