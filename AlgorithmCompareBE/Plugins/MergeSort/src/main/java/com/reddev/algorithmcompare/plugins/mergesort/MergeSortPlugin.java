package com.reddev.algorithmcompare.plugins.mergesort;

import com.reddev.algorithmcompare.plugins.pluginmodel.business.BasePlugin;
import lombok.extern.log4j.Log4j2;
import org.pf4j.PluginWrapper;

@Log4j2
public class MergeSortPlugin extends BasePlugin {

    public MergeSortPlugin(PluginWrapper wrapper) { super(wrapper, MergeSortConf.class); }

    @Override
    public void start() {
        log.info("MergeSortPlugin.start()");
    }

    @Override
    public void stop() {

        log.info("MergeSortPlugin.stop()");
        super.stop();

    }

}
