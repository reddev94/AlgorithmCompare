package com.reddev.algorithmcompare.common.domain.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArrayInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 4436885413026611550L;

    public int value;
    public String color;
}
