package com.ams.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleResourceNotFoundException(
      ResourceNotFoundException exception) {
    ApiError apiError = new ApiError(exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {
    ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InsufficientBalanceException.class)
  public ResponseEntity<ApiError> handleInsufficientBalanceException(
      InsufficientBalanceException ex) {
    ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    return new ResponseEntity<>(apiError, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex) {
    ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }
}
