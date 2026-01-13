package com.ams.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

  @Value("${kafka.topic.topup-created-topic}")
  private String TOPUP_CREATED_TOPIC;

  @Bean
  public NewTopic topupCreatedTopic() {
    return new NewTopic(TOPUP_CREATED_TOPIC, 3, (short) 1);
  }
}
