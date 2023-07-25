package com.reddev.algorithmcompare.core.domain.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteExecuteAlgorithmDataResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -1661264745773310319L;

    private int recordEliminated;

}
