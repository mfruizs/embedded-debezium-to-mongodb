package com.mfruizs.example.kafka;

import io.debezium.data.Envelope.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.connect.data.Struct;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static io.debezium.data.Envelope.FieldName.AFTER;
import static io.debezium.data.Envelope.FieldName.BEFORE;
import static io.debezium.data.Envelope.FieldName.OPERATION;
import static org.apache.commons.lang3.BooleanUtils.negate;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Component
public class KafkaStructAdapter {

	public static final String MONGO_KEY = "id";
	public static final String MONGO_OBJECT_KEY = "$oid";
	private static final String REGEX_QUOTES = "\"";

	public String obtainKeyFromKafkaStruct(Struct sourceRecordKey) {

		String identify = sourceRecordKey.getString(MONGO_KEY);

		if (isNecessaryToCleanIdentifier(identify)) {
			return identify.replace(REGEX_QUOTES, StringUtils.EMPTY);
		}

		return identify;
	}

	public String obtainValueFromKafkaStruct(Struct sourceRecordValue) {

		if (Objects.isNull(sourceRecordValue)) {
			return "{}";
		}

		return sourceRecordValue.getString(obtainOperationType(sourceRecordValue));
	}

	private boolean isNecessaryToCleanIdentifier(String identify) {
		return isNotBlank(identify)
				&& negate(identify.contains(MONGO_OBJECT_KEY));
	}

	private String obtainOperationType(Struct sourceRecordValue) {

		Operation operation = Operation.forCode((String) sourceRecordValue.get(OPERATION));
		return operation == Operation.DELETE ? BEFORE : AFTER;
	}

}
