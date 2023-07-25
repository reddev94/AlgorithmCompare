package com.reddev.algorithmcompare.core.handler;

import com.reddev.algorithmcompare.common.domain.exception.AlgorithmException;
import com.reddev.algorithmcompare.core.domain.rest.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Log4j2
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AlgorithmException.class)
    public ResponseEntity<ErrorResponseDTO> handleAlgorithmException(AlgorithmException ex, HttpServletRequest request) {

        log.error("Throwed algorithm exception: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(status)
                .body(getErrorDTO(ex, status, request));

    }

    private ErrorResponseDTO getErrorDTO(AlgorithmException e, HttpStatus status, HttpServletRequest request) {

        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .status(status.value())
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .message(e.getMessage())
                .error(e.getCode())
                .exception(e.getClass().getName())
                .build();
        log.debug(String.valueOf(errorResponseDTO));
        return errorResponseDTO;

    }
}
