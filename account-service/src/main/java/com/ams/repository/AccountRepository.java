package com.ams.repository;

import com.ams.entity.Account;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
  Optional<Account> findByAccountNumber(String accountNumber);

  boolean existsByAccountNumber(String accountNumber);
}
