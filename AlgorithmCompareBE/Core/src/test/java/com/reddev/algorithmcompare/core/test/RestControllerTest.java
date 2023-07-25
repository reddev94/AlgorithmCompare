package com.reddev.algorithmcompare.core.test;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.core.domain.rest.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Profile("test")
public class RestControllerTest extends AlgorithmCompareTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private AlgorithmCompareDAOImplTest algorithmCompareDAO;

    @Test
    public void testExecuteAllAlgorithms() {
        Random rand = new Random();
        //populate combobox of available algorithms
        GetAlgorithmResponse getAlgorithmResponse = webTestClient.get()
                .uri(TestUtil.PATH_GET_ALGORITHMS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GetAlgorithmResponse.class)
                .returnResult()
                .getResponseBody();
        assertThat(getAlgorithmResponse).isNotNull();
        assertThat(getAlgorithmResponse.getAvailableAlgorithms()).isNotNull().hasSize(AlgorithmEnum.values().length);
        //execute main flow for every algorithm
        for (String algorithm : getAlgorithmResponse.getAvailableAlgorithms()) {
            //generate casual array
            int length = rand.nextInt(AlgorithmCompareUtil.ARRAY_MAX_SIZE - AlgorithmCompareUtil.ARRAY_MIN_SIZE) + AlgorithmCompareUtil.ARRAY_MIN_SIZE;
            GenerateArrayResponse generateArrayResponse = webTestClient.get()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path(TestUtil.PATH_GENERATE_ARRAY)
                                    .queryParam(TestUtil.PARAM_LENGTH, length)
                                    .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(GenerateArrayResponse.class)
                    .returnResult()
                    .getResponseBody();
            assertThat(generateArrayResponse).isNotNull();
            assertThat(generateArrayResponse.getArray()).hasSize(length);
            //execute chosen algorithm on array
            ExecuteAlgorithmResponse executeAlgorithmResponse = webTestClient.post()
                    .uri(TestUtil.PATH_EXECUTE_ALGORITHM)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(TestUtil.forgeExecuteAlgorithmRequest(AlgorithmEnum.get(algorithm), generateArrayResponse.getArray())), ExecuteAlgorithmRequest.class)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(ExecuteAlgorithmResponse.class)
                    .returnResult()
                    .getResponseBody();
            assertThat(executeAlgorithmResponse).isNotNull();
            assertThat(executeAlgorithmResponse.getIdRequester()).isGreaterThan(0L);
            assertThat(executeAlgorithmResponse.getMaxExecutionTime()).isGreaterThan(0L);
            long idRequester = executeAlgorithmResponse.getIdRequester();
            //make getExecutionData delay fast for test
            long maxExecutionTime = 1000;
            Objects.requireNonNull(algorithmCompareDAO.findDocument(idRequester).collectList().block()).forEach(x -> {
                algorithmCompareDAO.saveDocumentTest(x.getId(), x.getArray(), x.getIdRequester(), 1, x.getMoveOrder(), x.getIndexOfSwappedElement()).block();
            });
            //get algorithm execution generated data, to display onscreen
            List<GetExecutionDataResponse> getExecutionDataResponse = webTestClient.get()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path(TestUtil.PATH_GET_EXECUTION_DATA)
                                    .queryParam(TestUtil.PARAM_ID_REQUESTER, idRequester)
                                    .queryParam(TestUtil.PARAM_MAX_MOVE_EXECUTION_TIME, maxExecutionTime)
                                    .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(GetExecutionDataResponse.class)
                    .returnResult()
                    .getResponseBody();
            assertThat(getExecutionDataResponse).isNotNull();
            for (GetExecutionDataResponse data : getExecutionDataResponse) {
                //TODO potrebbe servire verificare il nuovo campo aggiunto alal response, quello che torna processing = 1
                assertThat(data.getArray()).hasSizeBetween(0, length);
            }
            //delete all data of idRequester before close the app
            DeleteExecuteAlgorithmDataResponse deleteExecuteAlgorithmDataResponse = webTestClient.delete()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path(TestUtil.PATH_DELETE_ALGORITHM_DATA)
                                    .queryParam(TestUtil.PARAM_ID_REQUESTER, idRequester)
                                    .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(DeleteExecuteAlgorithmDataResponse.class)
                    .returnResult()
                    .getResponseBody();
            assertThat(deleteExecuteAlgorithmDataResponse).isNotNull();
            assertThat(deleteExecuteAlgorithmDataResponse.getRecordEliminated()).isGreaterThan(0);
        }

    }

}
