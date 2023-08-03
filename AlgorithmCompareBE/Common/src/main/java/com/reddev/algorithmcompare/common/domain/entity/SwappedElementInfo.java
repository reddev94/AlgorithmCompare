package com.reddev.algorithmcompare.common.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwappedElementInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 4436885413026611550L;

    private int index;
    private String color;
}
