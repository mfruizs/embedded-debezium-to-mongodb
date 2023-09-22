package com.mfruizs.example.mongo.configuration;

import static org.apache.commons.lang3.BooleanUtils.negate;
import static org.apache.commons.lang3.StringUtils.isAnyBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

import io.debezium.config.Configuration.Builder;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DebeziumConfig {

	private final MongoProperties mongoProperties;


	@Bean
	public io.debezium.config.Configuration mongodbConnector() throws IOException {

		return this.createConfiguration().build();
	}

	private Builder createConfiguration() throws IOException {

		final Builder debeziumConfigBuilder = io.debezium.config.Configuration.create()
			// engine properties
			.with("name", this.mongoProperties.getConnector().getName())
			.with("connector.class", this.mongoProperties.getConnector().getConnectorClass())
			.with("tasks.max", "1")
			.with("topic.prefix", this.mongoProperties.getKafka().getTopicPrefix());
		this.addMongoConfiguration(debeziumConfigBuilder);
		this.addKafkaConfiguration(debeziumConfigBuilder);

		if (this.isSecured()) {
			debeziumConfigBuilder.with("mongodb.user", this.mongoProperties.getConnector().getMongodb().getUser());
			debeziumConfigBuilder.with("mongodb.password", this.mongoProperties.getConnector().getMongodb().getPassword());
		}

		return debeziumConfigBuilder;
	}

	private void addMongoConfiguration(final Builder debeziumConfigBuilder) {

		debeziumConfigBuilder.with("errors.log.include.messages", "true")
			.with("mongodb.connection.string",
				  this.obtainSingleStringFromStringList(this.mongoProperties.getConnector().getMongodb().getHosts()));

		if (negate(isEmpty(this.mongoProperties.obtainDataBaseWhitelist()))) {
			debeziumConfigBuilder.with("database.include.list",
									   this.obtainSingleStringFromStringList(this.mongoProperties.obtainDataBaseWhitelist()));
		}

		if (negate(isEmpty(this.mongoProperties.obtainCollectionWhitelist()))) {
			debeziumConfigBuilder.with("collection.include.list",
									   this.obtainSingleStringFromStringList(this.mongoProperties.obtainCollectionWhitelist()));
		}

		if (negate(isEmpty((this.mongoProperties.obtainExcludeFieldList())))) {
			debeziumConfigBuilder.with("field.exclude.list",
									   this.obtainSingleStringFromStringList(this.mongoProperties.obtainExcludeFieldList()));
		}

	}

	private void addKafkaConfiguration(final Builder debeziumConfigBuilder) throws IOException {

		final File offsetStorageTempFile = File.createTempFile("offsets_", ".dat");

		debeziumConfigBuilder.with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
			.with("offset.storage.file.filename", offsetStorageTempFile.getAbsolutePath())
			.with("offset.flush.interval.ms", "60000")
			.with("tombstones.on.delete", false);
	}

	private boolean isSecured() {

		return negate(isAnyBlank(this.mongoProperties.getConnector().getMongodb().getUser(),
								 this.mongoProperties.getConnector().getMongodb().getPassword()));
	}

	private String obtainSingleStringFromStringList(final List<String> list) {

		return String.join(",", list);
	}

}
