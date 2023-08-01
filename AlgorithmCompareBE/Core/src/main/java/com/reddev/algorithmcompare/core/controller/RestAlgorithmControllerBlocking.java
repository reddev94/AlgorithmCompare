package com.reddev.algorithmcompare.core.controller;

import com.reddev.algorithmcompare.core.domain.rest.GenerateArrayResponse;
import com.reddev.algorithmcompare.core.domain.rest.GetAlgorithmResponse;
import com.reddev.algorithmcompare.core.service.RestAlgorithmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/blocking")
public class RestAlgorithmControllerBlocking {

    private final RestAlgorithmService restAlgorithmService;

    @GetMapping(value = "/getAlgorithms", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GetAlgorithmResponse getAvailableAlgorithms() {

        return restAlgorithmService.getAvailableAlgorithms();

    }

    @GetMapping(value = "/generateArray", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GenerateArrayResponse generateArray(@RequestParam int length) {

        return restAlgorithmService.generateArray(length);

    }

}
