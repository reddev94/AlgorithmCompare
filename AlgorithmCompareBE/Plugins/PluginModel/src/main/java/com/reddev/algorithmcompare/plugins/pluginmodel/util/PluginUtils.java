package com.reddev.algorithmcompare.plugins.pluginmodel.util;

import com.reddev.algorithmcompare.plugins.pluginmodel.AlgorithmConfiguration;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PluginUtils {

    private PluginUtils() {}

    public static ApplicationContext configureApplicationContext(PluginWrapper pluginWrapper, Class<? extends AlgorithmConfiguration> configuration) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // set the parent context (to access beans from application)
        applicationContext.setParent(((SpringPluginManager) pluginWrapper.getPluginManager()).getApplicationContext());
        applicationContext.setClassLoader(pluginWrapper.getPluginClassLoader());
        applicationContext.register(configuration);
        applicationContext.refresh();
        return applicationContext;
    }
}
