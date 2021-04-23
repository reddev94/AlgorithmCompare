package com.reddev.algorithmcompare.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.reddev.algorithmcompare.repository")
//@EnableTransactionManagement
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

//    @Bean("reactiveTransactionManager")
//    public ReactiveTransactionManager reactiveTransactionManager(ReactiveMongoDatabaseFactory factory) {
//        return new ReactiveMongoTransactionManager(factory);
//    }
}
