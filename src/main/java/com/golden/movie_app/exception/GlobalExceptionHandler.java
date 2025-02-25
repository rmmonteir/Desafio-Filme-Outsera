package com.golden.movie_app.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ProcessingException.class)
    public ResponseEntity<ApiErrorResponse> handleCsvProcessingException(ProcessingException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                messageSource.getMessage("csv.file.processing.error.sparam",null, null)
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                messageSource.getMessage("error.csv.file.size",null, null),
                HttpStatus.PAYLOAD_TOO_LARGE.value(),
                messageSource.getMessage("error.csv.file.size.large",null, null)
        );
        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                messageSource.getMessage("error.internal.server",null, null),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                messageSource.getMessage("error.internal",null, null)
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}