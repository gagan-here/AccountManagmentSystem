package com.ams.topup_service.service.impl;

import com.ams.topup_service.dto.TopupRequest;
import com.ams.topup_service.entity.Topup;
import com.ams.topup_service.repository.TopupRepository;
import com.ams.topup_service.service.TopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopupServiceImpl implements TopupService {

  private final ModelMapper modelMapper;
  private final TopupRepository topupRepository;

  @Override
  @Transactional
  public Topup topupAmount(TopupRequest topupRequest) {
    return null;
  }
}
