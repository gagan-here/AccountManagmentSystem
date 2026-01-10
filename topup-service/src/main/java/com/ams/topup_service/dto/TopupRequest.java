package com.ams.topup_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopupRequest {
  @NotBlank
  private String fromAccount;

  @NotBlank
  private String toAccount;

  @NotNull
  @DecimalMin(value = "10.00")
  private BigDecimal amount;
}
