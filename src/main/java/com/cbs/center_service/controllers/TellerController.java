package com.cbs.center_service.controllers;

import com.cbs.center_service.services.TellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tellers")
@CrossOrigin(origins = {"*"})
public class TellerController {


  @Autowired
  private TellerService tellerService;

  @RequestMapping(value = "/alls")
  public ResponseEntity<List<Map<String, Object>>> getTellers() {
    List<Map<String, Object>> tellers = tellerService.getTellers();
    if (tellers.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(tellers);
  }
}
