package com.reddev.algorithmcompare.plugins.bubblesort;

import com.reddev.algorithmcompare.plugins.pluginmodel.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BubbleSortConf {

    @Bean
    public Algorithm bubbleSortAlgorithm() {
        return new BubbleSortImpl();
    }

}
