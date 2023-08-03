package com.reddev.algorithmcompare.core.test;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.core.domain.rest.ExecuteAlgorithmRequest;
import org.springframework.context.annotation.Profile;

@Profile("test")
public class TestUtil {
    public static final String PATH_GET_ALGORITHMS = "/blocking/getAlgorithms";
    public static final String PATH_GENERATE_ARRAY = "/blocking/generateArray";
    public static final String PATH_EXECUTE_ALGORITHM = "/reactive/executeAlgorithm";
    public static final String PATH_GET_EXECUTION_DATA = "/reactive/getExecutionData";
    public static final String PATH_DELETE_ALGORITHM_DATA = "/reactive/deleteExecuteAlgorithmData";
    public static final String PARAM_LENGTH = "length";
    public static final String PARAM_ID_REQUESTER = "idRequester";
    public static final String PARAM_MAX_MOVE_EXECUTION_TIME = "maxMoveExecutionTime";

    public static ExecuteAlgorithmRequest forgeExecuteAlgorithmRequest(AlgorithmEnum algorithmEnum, int[] array, long idRequester) {
        return ExecuteAlgorithmRequest.builder().algorithm(algorithmEnum.getValue()).array(array).idRequester(idRequester).build();
    }

    public static ExecuteAlgorithmRequest forgeExecuteAlgorithmRequestInvalid(String algorithmEnum, int[] array, long idRequester) {
        return ExecuteAlgorithmRequest.builder().algorithm(algorithmEnum).array(array).idRequester(idRequester).build();
    }
}
