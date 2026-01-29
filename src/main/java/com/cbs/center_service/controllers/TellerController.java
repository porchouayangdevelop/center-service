package com.cbs.center_service.controllers;

import com.cbs.center_service.services.TellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tellers")
@Tag(name = "CBS Teller API", description = "APIs for managing CBS Tellers")
public class TellerController {


  @Autowired
  private TellerService tellerService;

  @Operation(summary = "Get All Tellers", description = "Retrieve a list of all CBS tellers", tags = {"CBS Teller API"})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Successfully retrieved list of tellers",
          content = @io.swagger.v3.oas.annotations.media.Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE
          )
      ),
      @ApiResponse(
          responseCode = "204",
          description = "No tellers found"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "Internal server error"
      )
  })

  @RequestMapping(value = "/alls")
  public ResponseEntity<List<Map<String, Object>>> getTellers() {
    List<Map<String, Object>> tellers = tellerService.getTellers();
    if (tellers.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(tellers);
  }
}
