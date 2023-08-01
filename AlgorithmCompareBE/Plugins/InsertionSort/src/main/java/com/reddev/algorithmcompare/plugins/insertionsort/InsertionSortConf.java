package com.reddev.algorithmcompare.plugins.insertionsort;

import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InsertionSortConf {

    @Bean
    public Algorithm insertionSortAlgorithm() {
        return new InsertionSortImpl();
    }

}
