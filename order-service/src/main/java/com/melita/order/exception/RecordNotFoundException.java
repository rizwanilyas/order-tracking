package com.melita.order.exception;

import com.melita.order.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class RecordNotFoundException extends AppException {

  public RecordNotFoundException(ErrorCode errorCode) {

    super(errorCode, HttpStatus.NOT_FOUND);
  }
}
