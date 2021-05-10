package com.reddev.algorithmcompare.core.controller.impl;

import com.reddev.algorithmcompare.core.business.RestAlgorithmBusiness;
import com.reddev.algorithmcompare.core.controller.RestAlgorithmControllerBlocking;
import com.reddev.algorithmcompare.core.controller.dto.GenerateArrayResponse;
import com.reddev.algorithmcompare.core.controller.dto.GetAlgorithmResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;

@RestController
@RequestMapping("/blocking")
public class RestAlgorithmControllerBlockingImpl implements RestAlgorithmControllerBlocking {
    Logger logger = LoggerFactory.getLogger(RestAlgorithmControllerBlockingImpl.class);

    @Autowired
    private RestAlgorithmBusiness restAlgorithmBusiness;

    @Override
    @GetMapping(value = "/getAlgorithms", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetAlgorithmResponse getAvailableAlgorithms() {
        logger.debug("- - - Entering RestAlgorithmControllerImpl.getAvailableAlgorithms() - - -");
        GetAlgorithmResponse response = restAlgorithmBusiness.getAvailableAlgorithms();
        logger.info("getAvailableAlgorithms response = " + response);
        logger.debug("- - - Exit RestAlgorithmControllerImpl.getAvailableAlgorithms() - - -");
        return response;
    }

    @Override
    @GetMapping(value = "/generateArray", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenerateArrayResponse generateArray(@RequestParam int length) {
        logger.debug("- - - Entering RestAlgorithmControllerImpl.generateArray() - - -");
        logger.info("generateArray request: length = " + length);
        GenerateArrayResponse response = restAlgorithmBusiness.generateArray(length);
        logger.info("generateArray response = " + response);
        logger.debug("- - - Exit RestAlgorithmControllerImpl.generateArray() - - -");
        return response;
    }

}
