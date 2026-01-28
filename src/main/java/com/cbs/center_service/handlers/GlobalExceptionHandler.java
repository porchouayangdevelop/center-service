package com.cbs.center_service.handlers;

import com.cbs.center_service.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<Response> handleBusinessException(BusinessException ex, WebRequest request) {
    log.error("Business exception occurred: {}", ex.getMessage());

    Response response = Response.builder()
        .code(ex.getCode())
        .message(ex.getMessage())
        .status("ERROR")
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(response, ex.getHttpStatus());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Response> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    log.error("Resource not found: {}", ex.getMessage());

    Response response = Response.builder()
        .code("RESOURCE_NOT_FOUND")
        .message(ex.getMessage())
        .status("ERROR")
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex, WebRequest request) {
    log.error("Validation exception: {}", ex.getMessage());

    Map<String, Object> response = new HashMap<>();
    response.put("code", "VALIDATION_ERROR");
    response.put("message", ex.getMessage());
    response.put("status", "");
    response.put("timestamp", "");
    response.put("errors", ex.getErrors());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DatabaseException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Response> handleDatabaseException(DatabaseException ex, WebRequest request) {
    log.error("Database exception occurred: {}", ex.getMessage(), ex);

    Response response = Response.builder()
        .code("DATABASE_ERROR")
        .message("A database error occurred. Please try again later")
        .status("ERROR")
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

  }

  @ExceptionHandler(SQLException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Response> handleSQLException(SQLException ex, WebRequest request) {
    log.error("SQL exception occurred: {}", ex.getMessage(), ex);

    Response response = Response.builder()
        .code("SQL_ERROR")
        .message("A database error occurred. Please contact support if the problem persists.")
        .status("ERROR")
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
    Map<String, Object> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(Collectors.toMap(FieldError::getField, fieldError -> fieldError.getDefaultMessage() != null
                ? fieldError.getDefaultMessage() : "Invalid value",
            (existing, replacement) -> existing
        ));

    log.error("Validation failed: {}", errors);

    Map<String, Object> response = new HashMap<>();
    response.put("code", "VALIDATION_FAILED");
    response.put("message", "Validation failed for one or more fields");
    response.put("status", "ERROR");
    response.put("timestamp", new Date());
    response.put("errors", errors);

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Response> handleMissingParams(MissingServletRequestParameterException ex, WebRequest request) {
    log.error("Missing request parameter: {}", ex.getParameterName());

    Response response = Response.builder()
        .code("MISSING_PARAMETER")
        .message(String.format("Required parameter '%s' is missing", ex.getParameterName()))
        .status("ERROR")
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Response> handleTypeMisMatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
    log.error("Type mismatch for parameter: {}", ex.getName());

    Response response = Response.builder()
        .code("TYPE_MISMATCH")
        .message(String.format("parameter '%s' is should be of type %s",
            ex.getName(),
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknow"
        ))
        .status("ERROR")
        .timestamp(new Date())
        .build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Response> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, WebRequest request) {

    log.error("Malformed JSON request: {}", ex.getMessage());

    Response response = Response.builder()
        .code("MALFORMED_REQUEST")
        .message("Malformed JSON request. Please check your request body.")
        .status("ERROR")
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Response> handleNoHandlerFound(
      NoHandlerFoundException ex, WebRequest request) {

    log.error("Endpoint not found: {} {}", ex.getHttpMethod(), ex.getRequestURL());

    Response response = Response.builder()
        .code("ENDPOINT_NOT_FOUND")
        .message(String.format("Endpoint '%s %s' not found",
            ex.getHttpMethod(),
            ex.getRequestURL()))
        .status("ERROR")
        .build();

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Response> handleIllegalArgument(
      IllegalArgumentException ex, WebRequest request) {
    log.error("Illegal argument: {}", ex.getMessage());

    Response response = Response.builder()
        .code("INVALID_ARGUMENT")
        .message(ex.getMessage())
        .status("ERROR")
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Response> handleNullPointer(
      NullPointerException ex, WebRequest request) {

    log.error("Null pointer exception occurred", ex);

    Response response = Response.builder()
        .code("NULL_POINTER_ERROR")
        .message("An unexpected error occurred. Please try again later.")
        .status("ERROR")
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }


  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Response> handleGlobalException(Exception ex, WebRequest request) {
    log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);

    Response response = Response.builder()
        .code("INTERNAL_SERVER_ERROR")
        .message("An unexpected error occurred. Please contact support if the problem persists.")
        .status("ERROR")
        .timestamp(new Date())
        .build();

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }


  private Map<String, Object> createResponse(Map<String, Object> response) {
    response.put("", "");

    return response;
  }
}
