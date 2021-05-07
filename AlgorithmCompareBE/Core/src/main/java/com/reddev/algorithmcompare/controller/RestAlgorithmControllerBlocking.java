package com.reddev.algorithmcompare.controller;


import com.reddev.algorithmcompare.controller.dto.DeleteExecuteAlgorithmDataResponse;
import com.reddev.algorithmcompare.controller.dto.GenerateArrayResponse;
import com.reddev.algorithmcompare.controller.dto.GetAlgorithmResponse;

public interface RestAlgorithmControllerBlocking {
    public GenerateArrayResponse generateArray(int length);
    public GetAlgorithmResponse getAvailableAlgorithms();
}
