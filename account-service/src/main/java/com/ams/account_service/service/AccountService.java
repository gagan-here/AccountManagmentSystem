package com.ams.account_service.service;

import com.ams.account_service.dto.AccountDto;
import com.ams.account_service.dto.CreateAccountRequest;

public interface AccountService {
  AccountDto createAccount(CreateAccountRequest accountRequest);
  AccountDto getAccountDetails(String accountNumber);
}
