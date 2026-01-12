package com.ams.topup_service.service.impl;

import com.ams.topup_service.dto.TopupRequest;
import com.ams.topup_service.event.TopupCreatedEvent;
import com.ams.topup_service.service.TopupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopupServiceImpl implements TopupService {
  @Value("${kafka.topic.topup-created-topic}")
  private String TOPUP_CREATED_TOPIC;

  private final ModelMapper modelMapper;
  private final KafkaTemplate<Long, TopupCreatedEvent> kafkaTemplate;

  @Override
  @Transactional
  public void topupAmount(TopupRequest topupRequest) {
    TopupCreatedEvent taskAssignedEvent = modelMapper.map(topupRequest, TopupCreatedEvent.class);
    kafkaTemplate.send(TOPUP_CREATED_TOPIC, taskAssignedEvent);
  }
}
