package com.ams.account_service.service.impl;

import com.ams.account_service.dto.AccountDto;
import com.ams.account_service.dto.CreateAccountRequest;
import com.ams.account_service.entity.Account;
import com.ams.account_service.event.TopupCreatedEvent;
import com.ams.account_service.exception.ResourceNotFoundException;
import com.ams.account_service.repository.AccountRepository;
import com.ams.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

  private final ModelMapper modelMapper;
  private final AccountRepository accountRepository;

  @Override
  @Transactional
  public AccountDto createAccount(CreateAccountRequest accountRequest) {
    Account account =
        accountRepository
            .findByAccountNumber(accountRequest.getAccountNumber())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Account already exists with account Number: "
                            + accountRequest.getAccountNumber()));
    return modelMapper.map(account, AccountDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public AccountDto getAccountDetails(String accountNumber) {
    Account account =
        accountRepository
            .findByAccountNumber(accountNumber)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Account not found with account number: " + accountNumber));
    return modelMapper.map(account, AccountDto.class);
  }

  @Override
  @Transactional
  public void topupAmount(TopupCreatedEvent event) {
    Account fromAccount =
        accountRepository
            .findByAccountNumber(event.getFromAccount())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Account not found with account number: " + event.getFromAccount()));
    fromAccount.setBalance(fromAccount.getBalance().subtract(event.getAmount()));

    Account toAccount =
        accountRepository
            .findByAccountNumber(event.getToAccount())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Account not found with account number: " + event.getToAccount()));
    toAccount.setBalance(toAccount.getBalance().add(event.getAmount()));



    accountRepository.save(fromAccount);
    accountRepository.save(toAccount);
  }
}
