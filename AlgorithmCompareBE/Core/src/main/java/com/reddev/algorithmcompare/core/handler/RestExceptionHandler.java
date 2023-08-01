package com.reddev.algorithmcompare.core.handler;

import com.reddev.algorithmcompare.common.domain.exception.DatabaseException;
import com.reddev.algorithmcompare.common.domain.exception.ValidationException;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
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

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorResponseDTO> handleDatabaseException(DatabaseException ex, HttpServletRequest request) {

        log.error("Threw database exception: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(status)
                .body(getErrorDTO(ex.getClass().getName(), status.value(), request.getRequestURI(), ex.getDescription(), ex.getCode()));

    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(ValidationException ex, HttpServletRequest request) {

        log.error("Threw validation exception: ", ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(getErrorDTO(ex.getClass().getName(), status.value(), request.getRequestURI(), ex.getDescription(), ex.getCode()));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex, HttpServletRequest request) {

        log.error("Threw exception: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(status)
                .body(getErrorDTO(ex.getClass().getName(), status.value(), request.getRequestURI(), AlgorithmCompareUtil.RESULT_DESCRIPTION_GENERIC_ERROR, AlgorithmCompareUtil.RESULT_CODE_GENERIC_ERROR));

    }

    private ErrorResponseDTO getErrorDTO(String ex, int status, String path, String errorDescription, int errorCode) {

        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .status(status)
                .path(path)
                .timestamp(Instant.now())
                .message(errorDescription)
                .error(errorCode)
                .exception(ex)
                .build();
        log.debug(String.valueOf(errorResponseDTO));
        return errorResponseDTO;

    }
}
