package com.reddev.algorithmcompare.plugins.mergesort;

import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MergeSortConf {

    @Bean
    public Algorithm mergeSortAlgorithm() {
        return new MergeSortImpl();
    }

}
