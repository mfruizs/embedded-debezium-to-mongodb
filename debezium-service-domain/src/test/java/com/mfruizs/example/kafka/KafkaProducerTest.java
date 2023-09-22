package com.mfruizs.example.kafka;

import static com.mfruizs.example.common.TestConstants.PARTITION_KEY;
import static com.mfruizs.example.common.TestConstants.PAYLOAD;
import static com.mfruizs.example.common.TestConstants.TOPIC;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

	@InjectMocks
	private KafkaProducer sut;

	@Mock
	private KafkaTemplate<Object, Object> kafkaTemplate;

	@Test
	void should_send_message_to_topic() {
		sut.publish(TOPIC, PARTITION_KEY, PAYLOAD);

		then(this.kafkaTemplate).should().send(TOPIC, PARTITION_KEY, PAYLOAD);

	}

}
