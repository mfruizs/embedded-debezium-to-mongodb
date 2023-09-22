package com.mfruizs.example.mongo.listener;

import com.mfruizs.example.kafka.KafkaProducer;
import com.mfruizs.example.kafka.KafkaStructAdapter;
import io.debezium.data.Envelope.Operation;
import io.debezium.engine.RecordChangeEvent;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.mfruizs.example.common.TestConstants.PARTITION_KEY;
import static com.mfruizs.example.common.TestConstants.PAYLOAD;
import static com.mfruizs.example.common.TestConstants.TOPIC;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DebeziumComponentTest {

	@InjectMocks
	private DebeziumComponent sut;
	@Mock
	private KafkaStructAdapter kafkaStructAdapter;
	@Mock
	private KafkaProducer kafkaProducer;
	@Mock
	private RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent;
	@Mock
	private SourceRecord sourceRecord;

	@Test
	void should_transform_and_send_data_to_kafka_on_update_operation(@Mock final Struct sourceRecordChangeValue) {

		given(this.sourceRecordRecordChangeEvent.record()).willReturn(this.sourceRecord);
		given(this.sourceRecord.topic()).willReturn(TOPIC);
		given(this.sourceRecord.value()).willReturn(sourceRecordChangeValue);
		given(sourceRecordChangeValue.get(anyString())).willReturn(Operation.UPDATE.code());
		given(this.kafkaStructAdapter.obtainKeyFromKafkaStruct(any())).willReturn(PARTITION_KEY);
		given(this.kafkaStructAdapter.obtainValueFromKafkaStruct(any())).willReturn(PAYLOAD);

		this.sut.handleChangeEvent(this.sourceRecordRecordChangeEvent);

		then(this.kafkaProducer).should().publish(TOPIC, PARTITION_KEY, PAYLOAD);

	}

	@Test
	void should_transform_and_send_data_to_kafka_on_update_operation_with_collection_name_as_partition_key(@Mock final Struct sourceRecordChangeValue) {

		given(this.sourceRecordRecordChangeEvent.record()).willReturn(this.sourceRecord);
		given(this.sourceRecord.topic()).willReturn(TOPIC);
		given(this.sourceRecord.value()).willReturn(sourceRecordChangeValue);
		given(sourceRecordChangeValue.get(anyString())).willReturn(Operation.UPDATE.code());
		given(this.kafkaStructAdapter.obtainKeyFromKafkaStruct(any())).willReturn(PARTITION_KEY);
		given(this.kafkaStructAdapter.obtainValueFromKafkaStruct(any())).willReturn(PAYLOAD);

		this.sut.handleChangeEvent(this.sourceRecordRecordChangeEvent);

		then(this.kafkaProducer).should().publish(TOPIC, PARTITION_KEY, PAYLOAD);

	}


}
