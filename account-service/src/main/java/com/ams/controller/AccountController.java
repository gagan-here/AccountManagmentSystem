package com.ams.controller;

import com.ams.dto.AccountDto;
import com.ams.dto.CreateAccountRequest;
import com.ams.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping
  public ResponseEntity<AccountDto> createAccount(
      @Valid @RequestBody CreateAccountRequest accountRequest) {
    AccountDto created = accountService.createAccount(accountRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{accountNumber}")
  public ResponseEntity<AccountDto> getAccountDetails(@PathVariable String accountNumber) {
    return ResponseEntity.ok(accountService.getAccountDetails(accountNumber));
  }
}
