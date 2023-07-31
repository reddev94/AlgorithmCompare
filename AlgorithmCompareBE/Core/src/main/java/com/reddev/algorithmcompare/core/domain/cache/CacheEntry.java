package com.reddev.algorithmcompare.core.domain.cache;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CacheEntry implements Serializable {

    @Serial
    private static final long serialVersionUID = 2565240358348092083L;

    private String name;
    private long ttl;
}
