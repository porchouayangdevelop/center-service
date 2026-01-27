package com.cbs.center_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api-test")
@Tag(name = "API Test", description = "Test endpoints to verify Scalar/OpenAPI is working")
public class ApiTestController {

  @GetMapping("/ping")
  @Operation(
      summary = "Ping endpoint",
      description = "Simple ping endpoint to test API connectivity"
  )
  @ApiResponse(responseCode = "200", description = "Successful ping")
  public String ping() {
    return "pong";
  }

  @GetMapping("/status")
  @Operation(
      summary = "Get API status",
      description = "Returns current API status with timestamp"
  )
  @ApiResponse(responseCode = "200", description = "Status retrieved successfully")
  public Map<String, Object> getStatus() {
    Map<String, Object> status = new HashMap<>();
    status.put("status", "UP");
    status.put("timestamp", LocalDateTime.now().toString());
    status.put("service", "Center Service");
    status.put("version", "1.0.0");
    return status;
  }

  @GetMapping("/echo/{message}")
  @Operation(
      summary = "Echo message",
      description = "Echoes back the provided message"
  )
  @ApiResponse(responseCode = "200", description = "Message echoed successfully")
  public Map<String, String> echo(@PathVariable String message) {
    Map<String, String> response = new HashMap<>();
    response.put("original", message);
    response.put("echoed", "You said: " + message);
    return response;
  }
}