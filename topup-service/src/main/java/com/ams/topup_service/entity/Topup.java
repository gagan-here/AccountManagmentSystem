package com.ams.topup_service.entity;

import com.ams.topup_service.enums.TopupStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "topups")
public class Topup {

  @Id @GeneratedValue private UUID id;

  @Column(name = "from_account", nullable = false)
  private String fromAccount;

  @Column(name = "to_account", nullable = false)
  private String toAccount;

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TopupStatus status;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public Topup() {}

  public Topup(String fromAccount, String toAccount, BigDecimal amount, TopupStatus status) {
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.amount = amount;
    this.status = status;
    this.createdAt = Instant.now();
  }
}
