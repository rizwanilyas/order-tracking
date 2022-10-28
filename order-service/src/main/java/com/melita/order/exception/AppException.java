package com.melita.order.exception;

import com.melita.order.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
  final private ErrorCode errorCode;
  final private HttpStatus status;

  public AppException(ErrorCode errorCode, HttpStatus status) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.status = status;
  }

  public AppException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
