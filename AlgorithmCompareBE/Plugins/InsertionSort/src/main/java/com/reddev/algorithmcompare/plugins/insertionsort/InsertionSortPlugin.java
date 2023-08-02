package com.reddev.algorithmcompare.plugins.insertionsort;

import com.reddev.algorithmcompare.plugins.pluginmodel.business.BasePlugin;
import lombok.extern.log4j.Log4j2;
import org.pf4j.PluginWrapper;
import org.springframework.cache.annotation.CacheEvict;

@Log4j2
public class InsertionSortPlugin extends BasePlugin {

    public InsertionSortPlugin(PluginWrapper wrapper) { super(wrapper, InsertionSortConf.class); }

    @Override
    @CacheEvict(value = "executeAlgorithm", allEntries = true)
    public void start() {
        log.info("InsertionSortPlugin.start()");
    }

    @Override
    public void stop() {

        log.info("InsertionSortPlugin.stop()");
        super.stop();

    }

}
