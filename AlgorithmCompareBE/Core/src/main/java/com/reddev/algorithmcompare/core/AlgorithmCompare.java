package com.reddev.algorithmcompare.core;

import com.reddev.algorithmcompare.common.domain.entity.AlgorithmDocument;
import com.reddev.algorithmcompare.common.repository.AlgorithmRepository;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.reddev"})
@RequiredArgsConstructor
@Log4j2
@EnableDiscoveryClient
public class AlgorithmCompare implements CommandLineRunner {

    private final AlgorithmRepository algorithmRepository;
    private final SpringPluginManager springPluginManager;

    public static void main(String[] args) {
        SpringApplication.run(AlgorithmCompare.class, args);
    }

    @Override
    public void run(String... args) {

        log.info("Insert document to initialize database");
        algorithmRepository.save(AlgorithmDocument.builder()
                .array(null)
                .idRequester(-1)
                .moveExecutionTime(0)
                .moveOrder(0)
                .build()).publishOn(AlgorithmCompareUtil.SCHEDULER)
                .block();
        algorithmRepository.deleteByIdRequester(-1).publishOn(AlgorithmCompareUtil.SCHEDULER).subscribe();

        //start plugin
        List<Algorithm> plugins = springPluginManager.getExtensions(Algorithm.class);
        log.info(String.format("Found %d extensions for extension point '%s'", plugins.size(), Algorithm.class.getName()));
        plugins.forEach(el -> log.info(">>> " + el.getName()));

    }
}
