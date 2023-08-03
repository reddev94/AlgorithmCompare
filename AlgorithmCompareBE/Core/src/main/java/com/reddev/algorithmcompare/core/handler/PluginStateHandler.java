package com.reddev.algorithmcompare.core.handler;

import com.reddev.algorithmcompare.core.util.CoreUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pf4j.PluginState;
import org.pf4j.PluginStateEvent;
import org.pf4j.PluginStateListener;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
@Log4j2
public class PluginStateHandler implements PluginStateListener {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void pluginStateChanged(PluginStateEvent pluginStateEvent) {

        log.debug("plugin state change event");
        if(pluginStateEvent.getPluginState().equals(PluginState.STARTED)) {
            log.debug("plugin started, deleting cache");
            CoreUtil.deleteRedisCache(CoreUtil.PLUGIN_ID_TO_ALGORITHM_MAP.get(pluginStateEvent.getPlugin().getPluginId()), redisTemplate);
        }

    }
}
