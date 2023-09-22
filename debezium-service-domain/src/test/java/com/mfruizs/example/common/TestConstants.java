package com.mfruizs.example.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants {

	public static final String TOPIC_PREFIX = "test.topic";
	public static final String COLLECTION_NAME = "TestDB.collectionTest";
	public static final String TOPIC = TOPIC_PREFIX + "." + COLLECTION_NAME;
	public static final String PARTITION_KEY = "ABC-123456";
	public static final String STRUCT_OBJECT_KEY = "\"_id\": { \"$oid\": \"6502eec8ef40b75b98f3b74d\" }";
	public static final String PAYLOAD = """
		{
		  "_id": 1,
		  "type": "dog",
		  "name": "Loula",
		  "color": "brown"
		}
		""";
}
