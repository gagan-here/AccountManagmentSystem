package com.ams.topup_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountDetails {
    private UUID id;
    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;

    public AccountDetails() {}
}
