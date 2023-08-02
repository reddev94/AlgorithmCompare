package com.reddev.algorithmcompare.core.util;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.common.domain.exception.ValidationException;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.core.domain.rest.ExecuteAlgorithmRequest;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public class RequestValidator {

    private RequestValidator() {}

    public static void checkArrayLength(int length) {

        if (length < AlgorithmCompareUtil.ARRAY_MIN_SIZE || length > AlgorithmCompareUtil.ARRAY_MAX_SIZE) {
            throw new ValidationException(AlgorithmCompareUtil.RESULT_CODE_ARRAY_LENGTH_ERROR, AlgorithmCompareUtil.RESULT_DESCRIPTION_ARRAY_LENGTH_ERROR);
        }

    }

    public static void checkExecuteAlgorithmRequest(ExecuteAlgorithmRequest request) {

        if (!StringUtils.hasText(request.getAlgorithm()) ||
                Arrays.stream(AlgorithmEnum.values()).noneMatch(el -> el.getValue().equals(request.getAlgorithm()))) {
            throw new ValidationException(AlgorithmCompareUtil.RESULT_CODE_INVALID_ALGORITHM, AlgorithmCompareUtil.RESULT_DESCRIPTION_INVALID_ALGORITHM);
        }

        checkArrayLength(request.getArray().length);

    }

    public static void checkIdRequester(long idRequester) {

        if (idRequester <= 0) {
            throw new ValidationException(AlgorithmCompareUtil.RESULT_CODE_INVALID_ID_REQUESTER, AlgorithmCompareUtil.RESULT_DESCRIPTION_INVALID_ID_REQUESTER);
        }

    }


}
