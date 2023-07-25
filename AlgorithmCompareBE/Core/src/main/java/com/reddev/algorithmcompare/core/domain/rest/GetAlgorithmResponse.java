package com.reddev.algorithmcompare.core.domain.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAlgorithmResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -8160514258898345914L;

    private List<String> availableAlgorithms;

}
