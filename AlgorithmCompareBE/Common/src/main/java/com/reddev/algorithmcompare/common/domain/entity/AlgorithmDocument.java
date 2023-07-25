package com.reddev.algorithmcompare.common.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Document
@Data
@Builder
public class AlgorithmDocument implements Serializable {

    @Serial
    private static final long serialVersionUID = -3381947381432657660L;

    @Id
    private String id;
    private long idRequester;
    private int[] array;
    private long moveOrder;
    private long moveExecutionTime;
    private int indexOfSwappedElement;
}
