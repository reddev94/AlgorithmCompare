package com.reddev.algorithmcompare.common.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
