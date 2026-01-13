package com.ams.event;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopupCreatedEvent {
  private String fromAccount;
  private String toAccount;
  private BigDecimal amount;
}
