package com.melita.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  ORDER_NOT_FOUND("ORDER_NOT_FOUND", "order not found with the given id"),
  PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "product not found with the given id"),
  PRODUCT_REQUIRED("PRODUCT_REQUIRED", "product is mandatory"),
  BUNDLED_REQUIRED("BUNDLED_REQUIRED", "bundle is mandatory"),
  PRODUCT_NOT_SUPPORT_BUNDLE("PRODUCT_NOT_SUPPORT_BUNDLE", "product not supported for this bundle");
  private String code;
  private String message;
}
