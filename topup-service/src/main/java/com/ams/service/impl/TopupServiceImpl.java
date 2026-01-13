package com.ams.service.impl;

import com.ams.dto.TopupRequest;
import com.ams.entity.Topup;
import com.ams.enums.TopupStatus;
import com.ams.event.TopupCreatedEvent;
import com.ams.repository.TopupRepository;
import com.ams.service.TopupService;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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
  private final TopupRepository topupRepository;

  private final ExecutorService publishAckExecutor = Executors.newFixedThreadPool(4);

  @Override
  @Transactional
  public void topupAmount(TopupRequest topupRequest) {
    TopupCreatedEvent topupCreatedEvent = modelMapper.map(topupRequest, TopupCreatedEvent.class);
    Topup topup =
        new Topup(
            topupRequest.getFromAccount(),
            topupRequest.getToAccount(),
            topupRequest.getAmount(),
            TopupStatus.PENDING);
    topup = topupRepository.save(topup);

    CompletableFuture<SendResult<Long, TopupCreatedEvent>> future =
        kafkaTemplate.send(TOPUP_CREATED_TOPIC, topupCreatedEvent);

    UUID topupId = topup.getId();

    future.whenCompleteAsync(
        (result, ex) -> {
          if (ex == null) {
            log.info(
                "Sent message for topup id=[{}] with offset=[{}]",
                topupId,
                result.getRecordMetadata().offset());

            // Update status to COMPLETED
            updateTopupStatus(topupId, TopupStatus.COMPLETED);

          } else {
            log.error(
                "Unable to send message for topup id=[{}] due to: {}",
                topupId,
                ex.getMessage(),
                ex);

            // Update status to FAILED
            updateTopupStatus(topupId, TopupStatus.FAILED);
          }
        },
        publishAckExecutor);
  }

  private void updateTopupStatus(UUID topupId, TopupStatus status) {
    try {
      topupRepository
          .findById(topupId)
          .ifPresentOrElse(
              topup -> {
                topup.setStatus(status);
                topupRepository.save(topup);
                log.info("Topup id={} marked as {}", topupId, status);
              },
              () ->
                  log.warn(
                      "Topup id={} not found in DB when updating status to {}", topupId, status));
    } catch (Exception ex) {
      log.error(
          "Failed to update topup id={} status to {}: {}", topupId, status, ex.getMessage(), ex);
    }
  }
}
