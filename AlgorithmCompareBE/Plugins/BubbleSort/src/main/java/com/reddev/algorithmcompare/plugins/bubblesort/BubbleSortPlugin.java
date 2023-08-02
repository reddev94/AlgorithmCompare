package com.reddev.algorithmcompare.plugins.bubblesort;

import com.reddev.algorithmcompare.plugins.pluginmodel.business.BasePlugin;
import lombok.extern.log4j.Log4j2;
import org.pf4j.PluginWrapper;
import org.springframework.cache.annotation.CacheEvict;

@Log4j2
public class BubbleSortPlugin extends BasePlugin {

    public BubbleSortPlugin(PluginWrapper wrapper) { super(wrapper, BubbleSortConf.class); }

    @Override
    @CacheEvict(value = "executeAlgorithm", allEntries = true)
    public void start() {
        log.info("BubbleSortPlugin.start()");
    }

    @Override
    public void stop() {

        log.info("BubbleSortPlugin.stop()");
        super.stop();

    }

}
