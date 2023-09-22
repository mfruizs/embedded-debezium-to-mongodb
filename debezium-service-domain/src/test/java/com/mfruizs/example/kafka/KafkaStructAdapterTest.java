package com.mfruizs.example.kafka;

import org.apache.kafka.connect.data.Struct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.mfruizs.example.common.TestConstants.PARTITION_KEY;
import static com.mfruizs.example.common.TestConstants.PAYLOAD;
import static com.mfruizs.example.common.TestConstants.STRUCT_OBJECT_KEY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KafkaStructAdapterTest {

	@InjectMocks
	private KafkaStructAdapter sut;

	@Test
	void should_obtain_topic_key_from_kafka_struct(@Mock Struct mockRecordKey) {

		given(mockRecordKey.getString(anyString())).willReturn("\"" + PARTITION_KEY + "\"");

		String key = sut.obtainKeyFromKafkaStruct(mockRecordKey);

		assertThat(key).isEqualTo(PARTITION_KEY);
	}

	@Test
	void should_obtain_topic_key_of_object_id_from_kafka_struct(@Mock Struct mockRecordKey) {

		given(mockRecordKey.getString(anyString())).willReturn(STRUCT_OBJECT_KEY);

		String key = sut.obtainKeyFromKafkaStruct(mockRecordKey);

		assertThat(key).isEqualTo(STRUCT_OBJECT_KEY);

	}

	@Test
	void should_parse_struct_value_json_field_to_bean(@Mock Struct mockRecordValue) {

		given(mockRecordValue.getString(anyString())).willReturn(PAYLOAD);

		Object result = sut.obtainValueFromKafkaStruct(mockRecordValue);
		assertThat(result).isEqualTo(PAYLOAD);

	}

	@Test
	void should_return_empty_object_on_null_entrance_parameter() {

		Object result = sut.obtainValueFromKafkaStruct(null);
		assertThat(result).isEqualTo("{}");

	}

}
