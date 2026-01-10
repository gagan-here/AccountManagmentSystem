package com.ams.topup_service.service;

import com.ams.topup_service.dto.TopupRequest;
import com.ams.topup_service.entity.Topup;

public interface TopupService {
  Topup topupAmount(TopupRequest topupRequest);
}
