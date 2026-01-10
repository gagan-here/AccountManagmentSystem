package com.ams.account_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {
  @NotBlank private String accountNumber;

  @NotBlank private String ownerName;

  @NotNull
  @DecimalMin(value = "0.0")
  private BigDecimal initialBalance;
}
