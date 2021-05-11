package com.reddev.algorithmcompare.core;


import com.reddev.algorithmcompare.dao.AlgorithmCompareDAO;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.conf.SpringPluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication(scanBasePackages= {
        "com.reddev.algorithmcompare.core",
        "com.reddev.algorithmcompare.commons",
        "com.reddev.algorithmcompare.dao"})
public class AlgorithmCompare implements CommandLineRunner {
    protected Logger logger = LoggerFactory.getLogger(AlgorithmCompare.class);

    @Autowired
    private AlgorithmCompareDAO algorithmCompareDAO;

    @Autowired
    private SpringPluginManager springPluginManager;

    public static void main(String[] args) {
        SpringApplication.run(AlgorithmCompare.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("Insert document to initialize database");
        algorithmCompareDAO.saveDocument(null, "-1", 0, 0, 0).block();
        algorithmCompareDAO.deleteDocument("-1").subscribe();
        //start plugin
        List<Algorithm> plugins = springPluginManager.getExtensions(Algorithm.class);
        logger.info(String.format("Found %d extensions for extension point '%s'", plugins.size(), Algorithm.class.getName()));
        for (Algorithm alg : plugins) {
            logger.info(">>> " + alg.getName());
        }
    }
}
