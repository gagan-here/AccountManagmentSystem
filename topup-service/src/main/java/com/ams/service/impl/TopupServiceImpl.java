package com.ams.service.impl;

import com.ams.dto.TopupRequest;
import com.ams.entity.Topup;
import com.ams.enums.TopupStatus;
import com.ams.event.TopupCreatedEvent;
import com.ams.repository.TopupRepository;
import com.ams.service.TopupService;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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

    CompletableFuture<SendResult<Long, TopupCreatedEvent>> sendFuture =
        kafkaTemplate.send(TOPUP_CREATED_TOPIC, topupCreatedEvent);

    // Submit a background task that waits for the broker (or times out) and updates DB accordingly
    Topup finalTopup = topup;
    CompletableFuture.runAsync(
        () -> {
          UUID topupId = finalTopup.getId();
          log.info("Starting async publish ack wait for topup id={}", topupId);
          try {
            log.debug("Waiting for sendFuture ack for topup id={} (timeout=10s)", topupId);
            SendResult<Long, TopupCreatedEvent> sr = sendFuture.get(10, TimeUnit.SECONDS);
            log.info("Publish ack received for topup id={} sendResult={}", topupId, sr);
            // On success, update status to PUBLISHED
            topupRepository
                .findById(finalTopup.getId())
                .ifPresent(
                    db -> {
                      db.setStatus(TopupStatus.COMPLETED);
                      topupRepository.save(db);
                      log.info("Topup id={} marked as COMPLETED", topupId);
                    });
          } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.warn(
                "Thread interrupted while waiting for publish ack for topup id={}", topupId, ie);
            topupRepository
                .findById(finalTopup.getId())
                .ifPresent(
                    db -> {
                      db.setStatus(TopupStatus.FAILED);
                      topupRepository.save(db);
                    });
          } catch (Exception ex) {
            log.error(
                "Error while waiting for publish ack for topup id={}: {}",
                topupId,
                ex.getMessage(),
                ex);
            Optional<Topup> opt = topupRepository.findById(topupId);
            if (opt.isPresent()) {
              Topup db = opt.get();
              db.setStatus(TopupStatus.FAILED);
              topupRepository.save(db);
              log.info("Topup id={} marked as FAILED due to exception", topupId);
            } else {
              log.warn("Topup id={} not found in DB when marking FAILED after exception", topupId);
            }
          }
        },
        publishAckExecutor);
  }
}
