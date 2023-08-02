package com.reddev.algorithmcompare.plugins.selectionsort;

import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import com.reddev.algorithmcompare.plugins.pluginmodel.AlgorithmConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SelectionSortConf implements AlgorithmConfiguration {

    @Bean
    public Algorithm algorithm() {
        return new SelectionSortImpl();
    }

}
