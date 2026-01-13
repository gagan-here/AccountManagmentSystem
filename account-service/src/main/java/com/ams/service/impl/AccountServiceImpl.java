package com.ams.service.impl;

import com.ams.dto.AccountDto;
import com.ams.dto.CreateAccountRequest;
import com.ams.entity.Account;
import com.ams.event.TopupCreatedEvent;
import com.ams.exception.BadRequestException;
import com.ams.exception.InsufficientBalanceException;
import com.ams.exception.ResourceNotFoundException;
import com.ams.repository.AccountRepository;
import com.ams.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    boolean exists = accountRepository.existsByAccountNumber(accountRequest.getAccountNumber());
    if (exists) {
      log.warn(
          "CreateAccount failed: account already exists accountNumber={}",
          accountRequest.getAccountNumber());
      throw new BadRequestException(
          "User with account number " + accountRequest.getAccountNumber() + " already exists");
    }

    Account account = modelMapper.map(accountRequest, Account.class);
    Account savedAccount = accountRepository.save(account);

    log.info("Account created successfully: accountNumber={}", savedAccount.getAccountNumber());

    return modelMapper.map(savedAccount, AccountDto.class);
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
    log.info(
        "Processing topup event: from={}, to={}, amount={}",
        event.getFromAccount(),
        event.getToAccount(),
        event.getAmount());

    try {
      Account fromAccount =
          accountRepository
              .findByAccountNumber(event.getFromAccount())
              .orElseThrow(
                  () -> {
                    log.warn("topupAmount: fromAccount not found from={}", event.getFromAccount());
                    return new ResourceNotFoundException(
                        "Account not found with account number: " + event.getFromAccount());
                  });
      fromAccount.setBalance(fromAccount.getBalance().subtract(event.getAmount()));

      if (fromAccount.getBalance().compareTo(event.getAmount()) < 0) {
        log.warn(
            "topupAmount: insufficient balance fromAccount={} currentBalance={} attemptedAmount={}",
            fromAccount.getAccountNumber(),
            fromAccount.getBalance(),
            event.getAmount());
        throw new InsufficientBalanceException("Insufficient balance");
      }

      Account toAccount =
          accountRepository
              .findByAccountNumber(event.getToAccount())
              .orElseThrow(
                  () -> {
                    log.warn("topupAmount: toAccount not found to={}", event.getToAccount());
                    return new ResourceNotFoundException(
                        "Account not found with account number: " + event.getToAccount());
                  });
      toAccount.setBalance(toAccount.getBalance().add(event.getAmount()));

      accountRepository.save(fromAccount);
      accountRepository.save(toAccount);

      log.info(
          "topupAmount completed: transferred={} from={} (newBalance={}) to={} (newBalance={})",
          event.getAmount(),
          fromAccount.getAccountNumber(),
          fromAccount.getBalance(),
          toAccount.getAccountNumber(),
          toAccount.getBalance());
    } catch (RuntimeException ex) {
      log.error("topupAmount failed: error={}", ex.getMessage(), ex);
      throw ex;
    }
  }
}
