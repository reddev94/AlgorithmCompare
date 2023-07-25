package com.reddev.algorithmcompare.core.controller;

import com.reddev.algorithmcompare.core.domain.rest.GenerateArrayResponse;
import com.reddev.algorithmcompare.core.domain.rest.GetAlgorithmResponse;
import com.reddev.algorithmcompare.core.service.RestAlgorithmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/blocking")
public class RestAlgorithmControllerBlocking {

    private final RestAlgorithmService restAlgorithmService;

    @GetMapping(value = "/getAlgorithms", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetAlgorithmResponse> getAvailableAlgorithms() {

        return ResponseEntity.ok(restAlgorithmService.getAvailableAlgorithms());

    }

    @GetMapping(value = "/generateArray", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenerateArrayResponse> generateArray(@RequestParam int length) {

        return ResponseEntity.ok(restAlgorithmService.generateArray(length));

    }

}
