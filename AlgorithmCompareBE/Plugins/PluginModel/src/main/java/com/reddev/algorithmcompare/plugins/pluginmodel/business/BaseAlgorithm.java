package com.reddev.algorithmcompare.plugins.pluginmodel.business;

import com.reddev.algorithmcompare.common.domain.entity.AlgorithmDocument;
import com.reddev.algorithmcompare.common.domain.entity.ArrayInfo;
import com.reddev.algorithmcompare.common.repository.AlgorithmRepository;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.plugins.pluginmodel.BaseAlgorithmExecutionData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.stream.IntStream;

@Log4j2
public class BaseAlgorithm {

    @Autowired
    private AlgorithmRepository algorithmRepository;

    protected long calculateTimestamp() {
        return AlgorithmCompareUtil.getTimestamp();
    }

    protected void saveInfo(BaseAlgorithmExecutionData data, int[] positions, String[] colors) {
        IntStream.range(0, positions.length).forEach(i -> data.getArray()[positions[i]].color = colors[i]);
        saveOnDb(data);
    }

    protected ArrayInfo[] convertToInfoArray(int[] input) {
        return Arrays.stream(input)
                .mapToObj(value -> new ArrayInfo(value, StringToColor.BLUE.getValue()))
                .toArray(ArrayInfo[]::new);
    }

    private void saveOnDb(BaseAlgorithmExecutionData data) {
        //calculate move execution time and save on db
        long actualTime = calculateTimestamp();
        long moveExecutionTime = (actualTime - data.getInitialTime()) + 1L; //add 1 millisecond to avoid 0 as moveExecutionTime
        saveRecord(data.getIdRequester(), data.getArray(), data.getMoveOrder(), moveExecutionTime);
        //save MAX moveExecutionTime
        if (data.getMaxExecutionTime() < moveExecutionTime) {
            data.setMaxExecutionTime(moveExecutionTime);
        }
        data.setMoveOrder(data.getMoveOrder() + 1);
        data.setInitialTime(actualTime);
        Arrays.stream(data.getArray()).forEach(el -> el.color = StringToColor.BLUE.getValue());
    }

    private void saveRecord(long idRequester, ArrayInfo[] array, long moveOrder, long moveExecutionTime) {
        algorithmRepository.save(AlgorithmDocument.builder()
                        .array(array)
                        .idRequester(idRequester)
                        .moveExecutionTime(moveExecutionTime)
                        .moveOrder(moveOrder)
                        .build()).publishOn(AlgorithmCompareUtil.SCHEDULER)
                .subscribe();
    }

}
