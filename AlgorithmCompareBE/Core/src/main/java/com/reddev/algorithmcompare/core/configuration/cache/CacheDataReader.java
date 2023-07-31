package com.reddev.algorithmcompare.core.configuration.cache;

import com.reddev.algorithmcompare.core.domain.cache.CacheEntry;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "cache", ignoreInvalidFields = true)
@Data
public class CacheDataReader {
    private List<CacheEntry> services;
}
