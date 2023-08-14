package com.tangent.tokens.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        logger.error("Unexpected error occurred: ", ex);
        return new ResponseEntity<>("500 Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException ex) {
        logger.debug("Invalid request: {}", ex.getMessage());
        return new ResponseEntity<>("400 Bad Request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException ex) {
        logger.debug("Object not found '{}'", ex.getMessage());
        return new ResponseEntity<>("404 Not Found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
