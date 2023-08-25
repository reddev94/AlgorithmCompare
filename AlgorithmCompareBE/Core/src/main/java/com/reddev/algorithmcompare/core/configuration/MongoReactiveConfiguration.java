package com.reddev.algorithmcompare.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.reddev.algorithmcompare.common.repository")
public class MongoReactiveConfiguration {
}
