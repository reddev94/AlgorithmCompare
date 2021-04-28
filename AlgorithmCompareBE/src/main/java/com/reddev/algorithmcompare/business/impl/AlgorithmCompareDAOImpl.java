package com.reddev.algorithmcompare.business.impl;

import com.reddev.algorithmcompare.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.business.AlgorithmCompareDAO;
import com.reddev.algorithmcompare.model.AlgorithmDocument;
import com.reddev.algorithmcompare.repository.AlgorithmRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
//@RequiredArgsConstructor
public class AlgorithmCompareDAOImpl implements AlgorithmCompareDAO {
    protected Logger logger = LoggerFactory.getLogger(AlgorithmCompareDAOImpl.class);

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Override
    //@Transactional(value = "reactiveTransactionManager", isolation = Isolation.REPEATABLE_READ)
    public Mono<AlgorithmDocument> saveDocument(int[] array, String idRequester, long moveExecutionTime, long moveOrder) {
        AlgorithmDocument request = new AlgorithmDocument();
        request.setArray(array);
        request.setIdRequester(idRequester);
        request.setMoveExecutionTime(moveExecutionTime);
        request.setMoveOrder(moveOrder);
        logger.debug("saving AlgorithmDocument = " + request.toString());
        return algorithmRepository.save(request).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }

    @Override
    public Flux<AlgorithmDocument> deleteDocument(String idRequester) {
        logger.debug("deleting AlgorithmDocument with idRequester = " + idRequester);
        return algorithmRepository.deleteByIdRequester(idRequester).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }

    @Override
    public Flux<AlgorithmDocument> findDocument(String idRequester) {
        logger.debug("finding AlgorithmDocument with idRequester = " + idRequester);
        return algorithmRepository.findByIdRequester(idRequester).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }

    @Override
    public Flux<AlgorithmDocument> findAll() {
        logger.debug("finding all AlgorithmDocument");
        return algorithmRepository.findAll().publishOn(AlgorithmCompareUtil.SCHEDULER);
    }
}
