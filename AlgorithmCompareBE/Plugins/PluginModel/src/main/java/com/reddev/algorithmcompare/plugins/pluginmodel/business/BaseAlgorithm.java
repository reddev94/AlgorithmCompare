package com.reddev.algorithmcompare.plugins.pluginmodel.business;

import com.reddev.algorithmcompare.common.domain.entity.AlgorithmDocument;
import com.reddev.algorithmcompare.common.repository.AlgorithmRepository;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.plugins.pluginmodel.BaseAlgorithmExecutionData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
public class BaseAlgorithm {

    @Autowired
    private AlgorithmRepository algorithmRepository;

    protected long calculateTimestamp() {
        return AlgorithmCompareUtil.getTimestamp();
    }

    protected void saveOnDb(BaseAlgorithmExecutionData data) {
        //calculate move execution time and save on db
        long actualTime = calculateTimestamp();
        long moveExecutionTime = (actualTime - data.getInitialTime()) + 1L; //add 1 millisecond to avoid 0 as moveExecutionTime
        saveRecord(data.getIdRequester(), data.getArray(), data.getMoveOrder(), moveExecutionTime, data.getIndexOfSwappedElement());
        //save MAX moveExecutionTime
        if (data.getMaxExecutionTime() < moveExecutionTime) {
            data.setMaxExecutionTime(moveExecutionTime);
        }
        data.setMoveOrder(data.getMoveOrder() + 1);
        data.setInitialTime(actualTime);
    }

    private void saveRecord(long idRequester, int[] array, long moveOrder, long moveExecutionTime, int indexOfSwappedElement) {
        algorithmRepository.save(AlgorithmDocument.builder()
                        .array(array)
                        .idRequester(idRequester).
                        moveExecutionTime(moveExecutionTime).
                        moveOrder(moveOrder)
                        .indexOfSwappedElement(indexOfSwappedElement)
                        .build()).publishOn(AlgorithmCompareUtil.SCHEDULER)
                .subscribe();
    }
}
