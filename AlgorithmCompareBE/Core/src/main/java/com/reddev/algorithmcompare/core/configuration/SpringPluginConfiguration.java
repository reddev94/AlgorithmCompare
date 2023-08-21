package com.reddev.algorithmcompare.core.configuration;

import com.reddev.algorithmcompare.core.handler.PluginStateHandler;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.file.Paths;

@Configuration
public class SpringPluginConfiguration {

    @Value("${plugin.path}")
    private String pluginPath;

    @Bean
    public SpringPluginManager customPluginManager(RedisTemplate<String, String> redisTemplate) {

        SpringPluginManager springPluginManager = new SpringPluginManager(Paths.get(pluginPath));
        springPluginManager.addPluginStateListener(new PluginStateHandler(redisTemplate));
        return springPluginManager;

    }
}
