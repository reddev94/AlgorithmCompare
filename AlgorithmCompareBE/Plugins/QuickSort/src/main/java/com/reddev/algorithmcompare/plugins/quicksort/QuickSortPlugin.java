package com.reddev.algorithmcompare.plugins.quicksort;

import lombok.extern.log4j.Log4j2;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Log4j2
public class QuickSortPlugin extends SpringPlugin {

    public QuickSortPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        log.info("QuickSortPlugin.start()");
    }

    @Override
    public void stop() {
        log.info("QuickSortPlugin.stop()");
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
