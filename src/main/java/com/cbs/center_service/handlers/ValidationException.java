package com.cbs.center_service.handlers;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {
  private final Map<String, String> errors;

  public ValidationException(String message) {
    super(message);
    this.errors = new HashMap<>();
  }

  public ValidationException(String message, Map<String, String> errors) {
    super(message);
    this.errors = errors;
  }

  public ValidationException(String field, String errorMessage) {
    super(String.format("validation failed for field '%s': %s", field, errorMessage));
    this.errors = new HashMap<>();
    this.errors.put(field, errorMessage);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
    this.errors = new HashMap<>();
  }

  public void addError(String field, String errorMessage) {
    this.errors.put(field, errorMessage);
  }

  public boolean hasErrors() {
    return !errors.isEmpty();
  }


}
