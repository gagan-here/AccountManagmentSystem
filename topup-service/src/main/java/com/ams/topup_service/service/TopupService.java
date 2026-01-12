package com.ams.topup_service.service;

import com.ams.topup_service.dto.TopupRequest;

public interface TopupService {
  void topupAmount(TopupRequest topupRequest);
}
