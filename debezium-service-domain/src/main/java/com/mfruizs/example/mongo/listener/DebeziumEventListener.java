package com.mfruizs.example.mongo.listener;

import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DebeziumEventListener {

	private final Executor executor;
	private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
	private final DebeziumComponent debeziumComponent;

	public DebeziumEventListener(final Configuration mongodbConnector, final DebeziumComponent debeziumComponent) {

		log.debug("DebeziumEventListener - RUN!");
		this.executor = Executors.newSingleThreadExecutor();

		this.debeziumComponent = debeziumComponent;

		this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
			.using(mongodbConnector.asProperties())
			.notifying(this::handleChangeEvent)
			.build();
	}

	private void handleChangeEvent(final RecordChangeEvent<SourceRecord> src) {

		this.debeziumComponent.handleChangeEvent(src);
	}

	@PostConstruct
	private void start() {

		this.executor.execute(this.debeziumEngine);
	}

	@PreDestroy
	private void stop() throws IOException {

		if (this.debeziumEngine != null) {
			this.debeziumEngine.close();
		}
	}
}

