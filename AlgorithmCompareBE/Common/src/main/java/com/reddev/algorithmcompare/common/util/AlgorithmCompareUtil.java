package com.reddev.algorithmcompare.common.util;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AlgorithmCompareUtil {

    private AlgorithmCompareUtil() {}

    public static final int ARRAY_MAX_SIZE = 30;
    public static final int ARRAY_MIN_SIZE = 10;
    public static final int ARRAY_MAX_VALUE = 98;

    public static final int RESULT_CODE_OK = 0;
    public static final int RESULT_CODE_PROCESSING = 1;
    public static final int RESULT_CODE_GENERIC_ERROR = -1;
    public static final String RESULT_DESCRIPTION_GENERIC_ERROR = "KO - generic error";
    public static final int RESULT_CODE_DB_ERROR = -2;
    public static final String RESULT_DESCRIPTION_DB_ERROR = "KO - error during db operation";
    public static final int RESULT_CODE_ARRAY_LENGTH_ERROR = -3;
    public static final String RESULT_DESCRIPTION_ARRAY_LENGTH_ERROR = "KO - invalid Array length, should be between " + ARRAY_MIN_SIZE + " and " + ARRAY_MAX_SIZE;
    public static final int RESULT_CODE_INVALID_ALGORITHM = -4;
    public static final String RESULT_DESCRIPTION_INVALID_ALGORITHM = "KO - invalid algorithm";
    public static final int RESULT_CODE_INVALID_ID_REQUESTER = -5;
    public static final String RESULT_DESCRIPTION_INVALID_ID_REQUESTER = "KO - invalid id requester";

    public static final Scheduler SCHEDULER = Schedulers.parallel();

    public static long getTimestamp() {
        return Instant.now().toEpochMilli();
    }

    public static long getTimestampMinusSeconds(long seconds) {
        return Instant.now().minusSeconds(seconds).toEpochMilli();
    }

    public static <T> Collector<T, ?, T> getCorrectAlgorithm() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

}
