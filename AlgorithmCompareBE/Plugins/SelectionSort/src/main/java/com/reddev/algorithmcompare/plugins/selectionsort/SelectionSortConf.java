package com.reddev.algorithmcompare.plugins.selectionsort;

import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SelectionSortConf {

    @Bean
    public Algorithm selectionSortAlgorithm() {
        return new SelectionSortImpl();
    }

}
