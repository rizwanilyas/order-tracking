package com.melita.order.exception;

import com.melita.order.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AppException.class)
  public ResponseEntity<ExceptionResponse> handleAppException(AppException exception) {
    logger.error(exception.getMessage(), exception);
    ErrorCode code = exception.getErrorCode();
    ExceptionResponse response =
        new ExceptionResponse(
            code.getMessage(), code.getCode(), exception.getStatus(), LocalDateTime.now());
    return new ResponseEntity<>(response, exception.getStatus());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException exception) {
    logger.error(exception.getMessage(), exception);
    ExceptionResponse response =
        new ExceptionResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleGeneralException(Exception exception) {
    logger.error(exception.getMessage(), exception);
    ExceptionResponse response =
        new ExceptionResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
