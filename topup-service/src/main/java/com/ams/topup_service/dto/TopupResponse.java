package com.ams.topup_service.dto;

import com.ams.topup_service.enums.TopupStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TopupResponse {
  private UUID id;
  private String fromAccount;
  private String toAccount;
  private BigDecimal amount;
  private TopupStatus status;
  private Instant createdAt;

  public TopupResponse() {}

  public TopupResponse(
      UUID id,
      String fromAccount,
      String toAccount,
      BigDecimal amount,
      TopupStatus status,
      Instant createdAt) {
    this.id = id;
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.amount = amount;
    this.status = status;
    this.createdAt = createdAt;
  }
}
