package com.reddev.algorithmcompare.plugins.quicksort;

import com.reddev.algorithmcompare.plugins.pluginmodel.business.BasePlugin;
import lombok.extern.log4j.Log4j2;
import org.pf4j.PluginWrapper;

@Log4j2
public class QuickSortPlugin extends BasePlugin {

    public QuickSortPlugin(PluginWrapper wrapper) { super(wrapper, QuickSortConf.class); }

    @Override
    public void start() {
        log.info("QuickSortPlugin.start()");
    }

    @Override
    public void stop() {

        log.info("QuickSortPlugin.stop()");
        super.stop();

    }

}
