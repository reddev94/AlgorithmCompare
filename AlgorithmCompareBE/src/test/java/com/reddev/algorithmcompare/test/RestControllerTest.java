package com.reddev.algorithmcompare.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.reddev.algorithmcompare.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.controller.dto.*;
import com.reddev.algorithmcompare.model.AlgorithmEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Random;

public class RestControllerTest extends AlgorithmCompareTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testExecuteAlgorithmBubbleSort() throws Exception {
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
        assertThat(getAlgorithmResponse.getResultCode()).isEqualTo(AlgorithmCompareUtil.RESULT_CODE_OK);
        assertThat(getAlgorithmResponse.getResultDescription()).isEqualTo(AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
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
            assertThat(generateArrayResponse.getResultCode()).isEqualTo(AlgorithmCompareUtil.RESULT_CODE_OK);
            assertThat(generateArrayResponse.getResultDescription()).isEqualTo(AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
            assertThat(generateArrayResponse.getArray()).hasSize(length);
            //execute chosen algorithm on array
            ExecuteAlgorithmResponse executeAlgorithmResponse = webTestClient.post()
                    .uri(TestUtil.PATH_EXECUTE_ALGORITHM)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(TestUtil.forgeExecuteAlgorithmRequest(AlgorithmEnum.valueOf(algorithm), generateArrayResponse.getArray())), ExecuteAlgorithmRequest.class)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(ExecuteAlgorithmResponse.class)
                    .returnResult()
                    .getResponseBody();
            assertThat(executeAlgorithmResponse).isNotNull();
            assertThat(executeAlgorithmResponse.getResultCode()).isEqualTo(AlgorithmCompareUtil.RESULT_CODE_OK);
            assertThat(executeAlgorithmResponse.getResultDescription()).isEqualTo(AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
            assertThat(executeAlgorithmResponse.getIdRequester()).isNotEmpty();
            String idRequester = executeAlgorithmResponse.getIdRequester();
            //get max execution time by previous algorithm generated idRequester, to generate new user-friendly move execution time
            GetMaxExecutionTimeResponse getMaxExecutionTimeResponse = webTestClient.get()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path(TestUtil.PATH_GET_MAX_EXECUTION_TIME)
                                    .queryParam(TestUtil.PARAM_ID_REQUESTER, idRequester)
                                    .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(GetMaxExecutionTimeResponse.class)
                    .returnResult()
                    .getResponseBody();
            assertThat(getMaxExecutionTimeResponse).isNotNull();
            assertThat(getMaxExecutionTimeResponse.getResultCode()).isEqualTo(AlgorithmCompareUtil.RESULT_CODE_OK);
            assertThat(getMaxExecutionTimeResponse.getResultDescription()).isEqualTo(AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
            assertThat(getMaxExecutionTimeResponse.getMaxExecutionTime()).isGreaterThan(0);
            //FE mapping new move execution time
            //get algorithm execution generated data, to display onscreen
            List<GetExecutionDataResponse> getExecutionDataResponse = webTestClient.get()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path(TestUtil.PATH_GET_EXECUTION_DATA)
                                    .queryParam(TestUtil.PARAM_ID_REQUESTER, idRequester)
                                    .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(GetExecutionDataResponse.class)
                    .returnResult()
                    .getResponseBody();
            assertThat(getExecutionDataResponse).isNotNull();
            for (GetExecutionDataResponse data : getExecutionDataResponse) {
                assertThat(data.getResultCode()).isEqualTo(AlgorithmCompareUtil.RESULT_CODE_OK);
                assertThat(data.getResultDescription()).isEqualTo(AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
                assertThat(data.getArray()).hasSize(length);
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
            assertThat(deleteExecuteAlgorithmDataResponse.getResultCode()).isEqualTo(AlgorithmCompareUtil.RESULT_CODE_OK);
            assertThat(deleteExecuteAlgorithmDataResponse.getResultDescription()).isEqualTo(AlgorithmCompareUtil.RESULT_DESCRIPTION_OK);
            assertThat(deleteExecuteAlgorithmDataResponse.getRecordEliminated()).isGreaterThan(0);
        }

    }

}
