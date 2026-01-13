package com.ams.controller;

import com.ams.dto.TopupRequest;
import com.ams.service.TopupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topup")
@RequiredArgsConstructor
public class TopupController {

  private final TopupService topupService;

  @PostMapping
  public ResponseEntity<String> topupAmount(@Valid @RequestBody TopupRequest topupRequest) {
    topupService.topupAmount(topupRequest);
    return ResponseEntity.ok("Topup event created successfully!");
  }
}
