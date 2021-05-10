package com.reddev.algorithmcompare.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.reddev.algorithmcompare.core.repository")
public class MongoReactiveConfiguration extends AbstractReactiveMongoConfiguration {
    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.name}")
    private String databaseName;

    @Override
    @Bean
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(databaseUrl);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }
}
