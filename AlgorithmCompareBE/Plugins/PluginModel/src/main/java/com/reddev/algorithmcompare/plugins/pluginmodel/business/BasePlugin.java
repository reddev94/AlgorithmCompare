package com.reddev.algorithmcompare.plugins.pluginmodel.business;

import com.reddev.algorithmcompare.plugins.pluginmodel.AlgorithmConfiguration;
import com.reddev.algorithmcompare.plugins.pluginmodel.util.PluginUtils;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.springframework.context.ApplicationContext;

public class BasePlugin extends SpringPlugin {

    private final PluginWrapper pluginWrapper;
    private final Class<? extends AlgorithmConfiguration> configurationClass;

    public BasePlugin(PluginWrapper wrapper, Class<? extends AlgorithmConfiguration> configurationClass) {
        super(wrapper);
        this.pluginWrapper = wrapper;
        this.configurationClass = configurationClass;
    }

    @Override
    protected ApplicationContext createApplicationContext() {
        return PluginUtils.configureApplicationContext(pluginWrapper, configurationClass);
    }

}
