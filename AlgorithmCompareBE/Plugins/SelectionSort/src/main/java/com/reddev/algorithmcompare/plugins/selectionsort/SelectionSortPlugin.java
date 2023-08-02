package com.reddev.algorithmcompare.plugins.selectionsort;

import com.reddev.algorithmcompare.plugins.pluginmodel.business.BasePlugin;
import lombok.extern.log4j.Log4j2;
import org.pf4j.PluginWrapper;
import org.springframework.cache.annotation.CacheEvict;

@Log4j2
public class SelectionSortPlugin extends BasePlugin {

    public SelectionSortPlugin(PluginWrapper wrapper) { super(wrapper, SelectionSortConf.class); }

    @Override
    @CacheEvict(value = "executeAlgorithm", allEntries = true)
    public void start() {
        log.info("SelectionSortPlugin.start()");
    }

    @Override
    public void stop() {

        log.info("SelectionSortPlugin.stop()");
        super.stop();

    }

}
