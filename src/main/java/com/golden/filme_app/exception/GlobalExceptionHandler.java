package com.golden.filme_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Tratar exceções de processamento de CSV
    @ExceptionHandler(CsvProcessingException.class)
    public ResponseEntity<ApiErrorResponse> handleCsvProcessingException(CsvProcessingException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro no processamento do CSV"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Tratar exceções de arquivo muito grande
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                "O arquivo enviado excede o tamanho máximo permitido.",
                HttpStatus.PAYLOAD_TOO_LARGE.value(),
                "Arquivo muito grande"
        );
        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    // Tratar exceções genéricas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                "Ocorreu um erro interno no servidor.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno"
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}