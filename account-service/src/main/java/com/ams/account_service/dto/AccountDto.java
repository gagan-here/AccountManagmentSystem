package com.ams.account_service.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class AccountDto {
  private UUID id;
  private String accountNumber;
  private String ownerName;
  private BigDecimal balance;
}
