package com.reddev.algorithmcompare.core.util;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

@Log4j2
public class CoreUtil {

    public static final Map<String, String> PLUGIN_ID_TO_ALGORITHM_MAP;

    static {
        // create a map between various plugin id (defined in the plugin.properties file) and the respective algorithm in the enum
        PLUGIN_ID_TO_ALGORITHM_MAP = new HashMap<>();
        PLUGIN_ID_TO_ALGORITHM_MAP.put("bubble-sort-plugin", AlgorithmEnum.BUBBLE_SORT.getValue());
        //TODO verificare l'esecuzione
        PLUGIN_ID_TO_ALGORITHM_MAP.put("insertion-sort-plugin", AlgorithmEnum.INSERTION_SORT.getValue());
        //TODO verificare l'esecuzione
        PLUGIN_ID_TO_ALGORITHM_MAP.put("merge-sort-plugin", AlgorithmEnum.MERGE_SORT.getValue());
        PLUGIN_ID_TO_ALGORITHM_MAP.put("quick-sort-plugin", AlgorithmEnum.QUICK_SORT.getValue());
        PLUGIN_ID_TO_ALGORITHM_MAP.put("selection-sort-plugin", AlgorithmEnum.SELECTION_SORT.getValue());
    }

    public static void deleteRedisCache(String pattern, RedisTemplate<String, String> redisTemplate) {
        log.debug("delete redis cache pattern = " + pattern);
        List<String> keys = new ArrayList<>(Objects.requireNonNull(redisTemplate.keys("*!" + pattern + "!*")));
        log.debug("keys to be deleted = " + keys.size());
        redisTemplate.delete(keys);
    }

}
