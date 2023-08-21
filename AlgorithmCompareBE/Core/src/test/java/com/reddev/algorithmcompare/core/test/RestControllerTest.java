package com.reddev.algorithmcompare.core.test;

import com.reddev.algorithmcompare.common.domain.business.AlgorithmEnum;
import com.reddev.algorithmcompare.common.domain.entity.AlgorithmDocument;
import com.reddev.algorithmcompare.common.repository.AlgorithmRepository;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import com.reddev.algorithmcompare.core.domain.rest.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureWebTestClient(timeout = "30000")
@AutoConfigureDataMongo
class RestControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Test
    void generateArrayLengthError() {
        ErrorResponseDTO errorResponseDTO = webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path(TestUtil.PATH_GENERATE_ARRAY)
                                .queryParam(TestUtil.PARAM_LENGTH, 60)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponseDTO.class)
                .returnResult()
                .getResponseBody();
        assertThat(errorResponseDTO).isNotNull();
        assertThat(errorResponseDTO.getMessage()).isEqualTo(AlgorithmCompareUtil.RESULT_DESCRIPTION_ARRAY_LENGTH_ERROR);
        assertThat(errorResponseDTO.getError()).isEqualTo(AlgorithmCompareUtil.RESULT_CODE_ARRAY_LENGTH_ERROR);
    }

    @Test
    void executeAlgorithmRequestAlgorithmInvalid() {
        ErrorResponseDTO errorResponseDTO = webTestClient.post()
                .uri(TestUtil.PATH_EXECUTE_ALGORITHM)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(TestUtil.forgeExecuteAlgorithmRequestInvalid("", new int[]{}, AlgorithmCompareUtil.getTimestamp())), ExecuteAlgorithmRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponseDTO.class)
                .returnResult()
                .getResponseBody();
        assertThat(errorResponseDTO).isNotNull();
        assertThat(errorResponseDTO.getMessage()).isEqualTo(AlgorithmCompareUtil.RESULT_DESCRIPTION_INVALID_ALGORITHM);
        assertThat(errorResponseDTO.getError()).isEqualTo(AlgorithmCompareUtil.RESULT_CODE_INVALID_ALGORITHM);
    }

    @Test
    void executeAlgorithmRequestArrayLength() {
        ErrorResponseDTO errorResponseDTO = webTestClient.post()
                .uri(TestUtil.PATH_EXECUTE_ALGORITHM)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(TestUtil.forgeExecuteAlgorithmRequest(AlgorithmEnum.get("QUICK SORT"), new int[]{}, AlgorithmCompareUtil.getTimestamp())), ExecuteAlgorithmRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponseDTO.class)
                .returnResult()
                .getResponseBody();
        assertThat(errorResponseDTO).isNotNull();
        assertThat(errorResponseDTO.getMessage()).isEqualTo(AlgorithmCompareUtil.RESULT_DESCRIPTION_ARRAY_LENGTH_ERROR);
        assertThat(errorResponseDTO.getError()).isEqualTo(AlgorithmCompareUtil.RESULT_CODE_ARRAY_LENGTH_ERROR);
    }

    @Test
    void getExecutionDataIdRequester() {
        ErrorResponseDTO errorResponseDTO = webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path(TestUtil.PATH_GET_EXECUTION_DATA)
                                .queryParam(TestUtil.PARAM_ID_REQUESTER, -1)
                                .queryParam(TestUtil.PARAM_MAX_MOVE_EXECUTION_TIME, 1000)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponseDTO.class)
                .returnResult()
                .getResponseBody();
        assertThat(errorResponseDTO).isNotNull();
        assertThat(errorResponseDTO.getMessage()).isEqualTo(AlgorithmCompareUtil.RESULT_DESCRIPTION_INVALID_ID_REQUESTER);
        assertThat(errorResponseDTO.getError()).isEqualTo(AlgorithmCompareUtil.RESULT_CODE_INVALID_ID_REQUESTER);
    }

    @Test
    void deleteExecutionDataIdRequester() {
        ErrorResponseDTO errorResponseDTO = webTestClient.delete()
                .uri(uriBuilder ->
                        uriBuilder
                                .path(TestUtil.PATH_DELETE_ALGORITHM_DATA)
                                .queryParam(TestUtil.PARAM_ID_REQUESTER, -1)
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponseDTO.class)
                .returnResult()
                .getResponseBody();
        assertThat(errorResponseDTO).isNotNull();
        assertThat(errorResponseDTO.getMessage()).isEqualTo(AlgorithmCompareUtil.RESULT_DESCRIPTION_INVALID_ID_REQUESTER);
        assertThat(errorResponseDTO.getError()).isEqualTo(AlgorithmCompareUtil.RESULT_CODE_INVALID_ID_REQUESTER);
    }

    @Test
    void testExecuteAllAlgorithms() {
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
                    .body(Mono.just(TestUtil.forgeExecuteAlgorithmRequest(AlgorithmEnum.get(algorithm), generateArrayResponse.getArray(), AlgorithmCompareUtil.getTimestamp())), ExecuteAlgorithmRequest.class)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(ExecuteAlgorithmResponse.class)
                    .returnResult()
                    .getResponseBody();
            assertThat(executeAlgorithmResponse).isNotNull();
            assertThat(executeAlgorithmResponse.getIdRequester()).isPositive();
            assertThat(executeAlgorithmResponse.getMaxExecutionTime()).isPositive();
            long idRequester = executeAlgorithmResponse.getIdRequester();
            //make getExecutionData delay fast for test
            long maxExecutionTime = 1000;
            Objects.requireNonNull(algorithmRepository.findByIdRequester(idRequester).collectList().block()).forEach(x -> {
                algorithmRepository.save(AlgorithmDocument.builder()
                        .id(x.getId())
                        .array(x.getArray())
                        .idRequester(x.getIdRequester())
                        .moveExecutionTime(1)
                        .moveOrder(x.getMoveOrder())
                        .build()).block();

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
            assertThat(deleteExecuteAlgorithmDataResponse.getRecordEliminated()).isPositive();
        }

    }

}
