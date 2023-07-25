package com.reddev.algorithmcompare.core.configuration;

import com.reddev.algorithmcompare.plugins.pluginmodel.conf.SpringPluginManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
public class SpringPluginConfiguration {

    @Value("${plugin.path}")
    private String pluginPath;

    @Bean
    public SpringPluginManager customPluginManager() {
        return new SpringPluginManager(Paths.get(pluginPath));
    }
}
