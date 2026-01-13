package com.ams.service;

import com.ams.dto.AccountDto;
import com.ams.dto.CreateAccountRequest;
import com.ams.event.TopupCreatedEvent;

public interface AccountService {
  AccountDto createAccount(CreateAccountRequest accountRequest);

  AccountDto getAccountDetails(String accountNumber);

  void topupAmount(TopupCreatedEvent event);
}
