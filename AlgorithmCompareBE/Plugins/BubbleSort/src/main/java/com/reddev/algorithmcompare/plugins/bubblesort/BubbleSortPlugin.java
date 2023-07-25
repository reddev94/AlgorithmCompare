package com.reddev.algorithmcompare.plugins.bubblesort;

import com.reddev.algorithmcompare.plugins.pluginmodel.conf.SpringPlugin;
import com.reddev.algorithmcompare.plugins.pluginmodel.conf.SpringPluginManager;
import lombok.extern.log4j.Log4j2;
import org.pf4j.PluginWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Log4j2
public class BubbleSortPlugin extends SpringPlugin {

    public BubbleSortPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        log.info("BubbleSortPlugin.start()");
    }

    @Override
    public void stop() {
        log.info("BubbleSortPlugin.stop()");
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
