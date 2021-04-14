package com.reddev.algorithmcompare;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AlgorithmCompareUtil {
    public static final int RESULT_CODE_OK = 0;
    public static final int RESULT_CODE_KO_GENERIC_ERROR = -1;
    public static final int RESULT_CODE_KO_DB_ERROR = -2;
    public static final int RESULT_CODE_KO_ARRAY_LENGTH_ERROR = -3;
    public static final String RESULT_DESCRIPTION_OK = "OK - Success";
    public static final String RESULT_DESCRIPTION_KO_GENERIC_ERROR = "KO - Generic error";
    public static final String RESULT_DESCRIPTION_KO_DB_ERROR = "KO - Error during db insert";
    public static final String RESULT_DESCRIPTION_KO_ARRAY_LENGTH_ERROR = "KO - Invalid Array length, should be between 10 and 100";
    public static final int ARRAY_MAX_SIZE = 101;
    public static final int ARRAY_MIN_SIZE = 10;
    public static final int ARRAY_MAX_VALUE = 999;

    public static final Scheduler SCHEDULER = Schedulers.boundedElastic();

    public static long getTimestamp() {
        return Instant.now().toEpochMilli();
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
