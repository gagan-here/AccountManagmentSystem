package com.ams.consumer;

import com.ams.event.TopupCreatedEvent;
import com.ams.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountConsumer {

  private final AccountService accountService;

  @RetryableTopic(
      attempts = "4",
      backoff = @Backoff(delay = 2000, multiplier = 1.5, maxDelay = 10000))
  @KafkaListener(topics = "topup-created-topic", groupId = "account-service")
  public void listenTopupAmount(
      TopupCreatedEvent event, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    try {
      log.info("Received topup created event: {}, topic {} ", event, topic);

      accountService.topupAmount(event);
    } catch (Exception e) {
      throw new RuntimeException("Error processing topup created event: " + e.getMessage());
    }
  }

  @DltHandler
  public void listenDLT(
      TopupCreatedEvent event, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    log.info("DLT received : {}, topic {}", event, topic);
  }
}
