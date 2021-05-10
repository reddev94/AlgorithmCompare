package com.reddev.algorithmcompare.core;

import com.reddev.algorithmcompare.core.business.AlgorithmCompareDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AlgorithmCompare implements CommandLineRunner {
    protected Logger logger = LoggerFactory.getLogger(AlgorithmCompare.class);

    @Autowired
    private AlgorithmCompareDAO algorithmCompareDAO;

    public static void main(String[] args) {
        SpringApplication.run(AlgorithmCompare.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("Insert document to initialize database");
        algorithmCompareDAO.saveDocument(null, "-1", 0, 0, 0).block();
        algorithmCompareDAO.deleteDocument("-1").subscribe();
    }
}
