package com.reddev.algorithmcompare.core.test;

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

        return new SpringPluginManager(Paths.get(pluginPath));

    }

}
