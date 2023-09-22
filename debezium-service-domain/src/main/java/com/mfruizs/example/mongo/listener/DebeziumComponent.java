package com.mfruizs.example.mongo.listener;

import static io.debezium.data.Envelope.FieldName.OPERATION;
import static io.debezium.data.Envelope.Operation;

import com.mfruizs.example.kafka.KafkaProducer;
import com.mfruizs.example.kafka.KafkaStructAdapter;
import io.debezium.engine.RecordChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DebeziumComponent {

	private final KafkaStructAdapter kafkaStructAdapter;
	private final KafkaProducer kafkaProducer;

	public void handleChangeEvent(final RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {

		final SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
		if (this.isPermittedOperation(sourceRecord)) {
			return;
		}

		final Struct sourceRecordKey = (Struct) sourceRecord.key();
		final Struct sourceRecordValue = (Struct) sourceRecord.value();
		log.debug("Received - Key: {} | Value: {}", sourceRecordKey, sourceRecordValue);

		final String partitionKey = this.kafkaStructAdapter.obtainKeyFromKafkaStruct((Struct) sourceRecord.key());
		final String payload = this.kafkaStructAdapter.obtainValueFromKafkaStruct(sourceRecordValue);

		this.kafkaProducer.publish(sourceRecord.topic(), partitionKey, payload);

	}

	private boolean isPermittedOperation(final SourceRecord sourceRecord) {

		final Struct sourceRecordValue = (Struct) sourceRecord.value();
		return this.isReadOperation(sourceRecordValue);
	}

	private boolean isReadOperation(final Struct sourceRecordValue) {

		return Operation.READ == Operation.forCode((String) sourceRecordValue.get(OPERATION));
	}

}
