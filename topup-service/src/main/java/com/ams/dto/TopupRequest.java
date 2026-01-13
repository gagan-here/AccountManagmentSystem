package com.ams.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class TopupRequest {
  @NotBlank private String fromAccount;

  @NotBlank private String toAccount;

  @NotNull
  @DecimalMin(value = "10.00")
  private BigDecimal amount;
}
