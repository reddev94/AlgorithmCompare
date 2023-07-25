package com.reddev.algorithmcompare.core.test;

import com.reddev.algorithmcompare.common.domain.entity.AlgorithmDocument;
import com.reddev.algorithmcompare.common.repository.AlgorithmRepository;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Profile("test")
@Service
@Log4j2
@RequiredArgsConstructor
public class AlgorithmCompareDAOImplTest {

    private final AlgorithmRepository algorithmRepository;

    public Mono<AlgorithmDocument> saveDocument(int[] array, long idRequester, long moveExecutionTime, long moveOrder, int indexOfSwappedElement) {
        log.debug("saving AlgorithmDocument");
        return algorithmRepository.save(AlgorithmDocument.builder()
                .array(array)
                .idRequester(idRequester)
                .moveExecutionTime(moveExecutionTime)
                .moveOrder(moveOrder)
                .indexOfSwappedElement(indexOfSwappedElement)
                .build()
        ).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }

    public Flux<AlgorithmDocument> deleteDocument(long idRequester) {
        log.debug("deleting AlgorithmDocument with idRequester = " + idRequester);
        return algorithmRepository.deleteByIdRequester(idRequester).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }

    public Flux<AlgorithmDocument> findDocument(long idRequester) {
        log.debug("finding AlgorithmDocument with idRequester = " + idRequester);
        return algorithmRepository.findByIdRequester(idRequester).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }
    
    public Flux<AlgorithmDocument> findAll() {
        log.debug("finding all AlgorithmDocument");
        return algorithmRepository.findAll().publishOn(AlgorithmCompareUtil.SCHEDULER);
    }
    
    public Flux<AlgorithmDocument> deleteOldData(long idRequester) {
        log.debug("deleting old db data");
        return algorithmRepository.deleteWhereIdRequesterLtThanMaxOldTime(idRequester).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }

    public Mono<AlgorithmDocument> saveDocumentTest(String id, int[] array, long idRequester, long moveExecutionTime, long moveOrder, int indexOfSwappedElement) {
        log.debug("saving AlgorithmDocument");
        return algorithmRepository.save(AlgorithmDocument.builder()
                .id(id)
                .array(array)
                .idRequester(idRequester)
                .moveExecutionTime(moveExecutionTime)
                .moveOrder(moveOrder)
                .indexOfSwappedElement(indexOfSwappedElement)
                .build()
        ).publishOn(AlgorithmCompareUtil.SCHEDULER);
    }
}
