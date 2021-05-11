package com.reddev.algorithmcompare.plugins.quicksort;

import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuickSortConf {

    @Bean
    public Algorithm quickSortAlgorithm() {
        return new QuickSortImpl();
    }

}
