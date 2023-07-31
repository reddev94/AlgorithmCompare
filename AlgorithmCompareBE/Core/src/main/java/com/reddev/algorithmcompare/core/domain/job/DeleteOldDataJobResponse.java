package com.reddev.algorithmcompare.core.domain.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteOldDataJobResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2264711412835103696L;

    private int deletedElement;
}
