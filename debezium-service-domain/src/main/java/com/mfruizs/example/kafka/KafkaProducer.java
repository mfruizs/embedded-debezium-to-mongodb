package com.mfruizs.example.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

	private final KafkaTemplate<Object, Object> kafkaTemplate;

	public void publish(final String topic, final String partitionKey, final String payload) {

		log.debug("Publish - Topic: {} | PartitionKey: {} | Payload: {}", topic, partitionKey, payload);
		this.kafkaTemplate.send(topic, partitionKey, payload);
	}

}
