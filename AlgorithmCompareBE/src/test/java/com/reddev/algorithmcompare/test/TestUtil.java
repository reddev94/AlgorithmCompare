package com.reddev.algorithmcompare.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.reddev.algorithmcompare.controller.dto.ExecuteAlgorithmRequest;
import com.reddev.algorithmcompare.model.AlgorithmEnum;

import java.util.Random;

public class TestUtil {
    public static final String PATH_GET_ALGORITHMS = "/blocking/getAlgorithms";
    public static final String PATH_GENERATE_ARRAY = "/blocking/generateArray";
    public static final String PATH_EXECUTE_ALGORITHM = "/reactive/executeAlgorithm";
    public static final String PATH_GET_MAX_EXECUTION_TIME = "/reactive/getMaxExecutionTime";
    public static final String PATH_GET_EXECUTION_DATA = "/reactive/getExecutionData";
    public static final String PATH_DELETE_ALGORITHM_DATA = "/reactive/deleteExecuteAlgorithmData";
    public static final String PARAM_LENGTH = "length";
    public static final String PARAM_ID_REQUESTER = "idRequester";

    public static ExecuteAlgorithmRequest forgeExecuteAlgorithmRequest(AlgorithmEnum algorithmEnum, int[] array) throws JsonProcessingException {
        ExecuteAlgorithmRequest requestObj = new ExecuteAlgorithmRequest();
        requestObj.setAlgorithm(algorithmEnum);
        requestObj.setArray(array);
        return requestObj;
    }
}
