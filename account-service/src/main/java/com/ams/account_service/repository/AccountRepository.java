package com.ams.account_service.repository;

import com.ams.account_service.entity.Account;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
  Optional<Account> findByAccountNumber(String accountNumber);
}
