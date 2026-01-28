package com.cbs.center_service.handlers;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s not found with %s: '%'", resourceName, fieldValue, fieldValue));
  }

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
