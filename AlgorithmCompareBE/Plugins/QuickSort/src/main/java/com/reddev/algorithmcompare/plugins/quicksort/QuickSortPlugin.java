package com.reddev.algorithmcompare.plugins.quicksort;

import com.reddev.algorithmcompare.plugins.pluginmodel.conf.SpringPlugin;
import com.reddev.algorithmcompare.plugins.pluginmodel.conf.SpringPluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class QuickSortPlugin extends SpringPlugin {
    Logger logger = LoggerFactory.getLogger(QuickSortPlugin.class);

    public QuickSortPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        logger.info("QuickSortPlugin.start()");
    }

    @Override
    public void stop() {
        logger.info("QuickSortPlugin.stop()");
        super.stop(); // to close applicationContext
    }

    @Override
    protected ApplicationContext createApplicationContext() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // set the parent context (to access beans from application)
        applicationContext.setParent(((SpringPluginManager) getWrapper().getPluginManager()).getApplicationContext());
        applicationContext.setClassLoader(getWrapper().getPluginClassLoader());
        applicationContext.register(QuickSortConf.class);
        applicationContext.refresh();
        return applicationContext;
    }

}
