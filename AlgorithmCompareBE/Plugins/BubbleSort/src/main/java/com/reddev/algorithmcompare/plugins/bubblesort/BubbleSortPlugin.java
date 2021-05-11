package com.reddev.algorithmcompare.plugins.bubblesort;

import com.reddev.algorithmcompare.plugins.pluginmodel.conf.SpringPlugin;
import com.reddev.algorithmcompare.plugins.pluginmodel.conf.SpringPluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BubbleSortPlugin extends SpringPlugin {
    Logger logger = LoggerFactory.getLogger(BubbleSortPlugin.class);

    public BubbleSortPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        logger.info("BubbleSortPlugin.start()");
    }

    @Override
    public void stop() {
        logger.info("BubbleSortPlugin.stop()");
        super.stop(); // to close applicationContext
    }

    @Override
    protected ApplicationContext createApplicationContext() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // set the parent context (to access beans from application)
        applicationContext.setParent(((SpringPluginManager) getWrapper().getPluginManager()).getApplicationContext());
        applicationContext.setClassLoader(getWrapper().getPluginClassLoader());
        applicationContext.register(BubbleSortConf.class);
        applicationContext.refresh();
        return applicationContext;
    }

}
