package com.ams.account_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "accounts",
    indexes = {@Index(name = "idx_account_number", columnList = "account_number", unique = true)})
public class Account {
  @Id @GeneratedValue private UUID id;

  @Column(name = "account_number", nullable = false, unique = true)
  private String accountNumber;

  @Column(name = "owner_name", nullable = false)
  private String ownerName;

  @Column(name = "balance", nullable = false, precision = 19, scale = 4)
  private BigDecimal balance;

  public Account(String accountNumber, String ownerName, BigDecimal balance) {
    this.accountNumber = accountNumber;
    this.ownerName = ownerName;
    this.balance = balance;
  }
}
