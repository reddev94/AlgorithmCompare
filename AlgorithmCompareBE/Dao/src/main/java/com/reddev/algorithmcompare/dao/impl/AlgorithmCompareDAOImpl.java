package com.reddev.algorithmcompare.dao.impl;

import com.reddev.algorithmcompare.commons.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.dao.model.AlgorithmDocument;
import com.reddev.algorithmcompare.dao.AlgorithmCompareDAO;
import com.reddev.algorithmcompare.dao.repository.AlgorithmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Profile("prod")
@Service
public class AlgorithmCompareDAOImpl implements AlgorithmCompareDAO {
    protected Logger logger = LoggerFactory.getLogger(AlgorithmCompareDAOImpl.class);

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Override
    public Mono<AlgorithmDocument> saveDocument(int[] array, long idRequester, long moveExecutionTime, long moveOrder, int indexOfSwappedElement) {
        AlgorithmDocument request = new AlgorithmDocument();
        request.setArray(array);
        request.setIdRequester(idRequester);
        request.setMoveExecutionTime(moveExecutionTime);
        request.setMoveOrder(moveOrder);
        request.setIndexOfSwappedElement(indexOfSwappedElement);
        logger.debug("saving AlgorithmDocument = " + request.toString());
        return algorithmRepository.save(request).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }

    @Override
    public Flux<AlgorithmDocument> deleteDocument(long idRequester) {
        logger.debug("deleting AlgorithmDocument with idRequester = " + idRequester);
        return algorithmRepository.deleteByIdRequester(idRequester).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }

    @Override
    public Flux<AlgorithmDocument> findDocument(long idRequester) {
        logger.debug("finding AlgorithmDocument with idRequester = " + idRequester);
        return algorithmRepository.findByIdRequester(idRequester).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }

    @Override
    public Flux<AlgorithmDocument> findAll() {
        logger.debug("finding all AlgorithmDocument");
        return algorithmRepository.findAll().publishOn(AlgorithmCompareUtil.SCHEDULER);
    }

    @Override
    public Flux<AlgorithmDocument> deleteOldData(long idRequester) {
        logger.debug("deleting old db data");
        return algorithmRepository.deleteWhereIdRequesterLtThanMaxOldTime(idRequester).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }

}
