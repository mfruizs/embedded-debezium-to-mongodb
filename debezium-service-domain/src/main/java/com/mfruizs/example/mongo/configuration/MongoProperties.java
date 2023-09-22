package com.mfruizs.example.mongo.configuration;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "debezium")
public class MongoProperties {

	private Connector connector;
	private Database database;
	private Kafka kafka;

	@Data
	public static class Connector {

		private String name;
		private String connectorClass;
		private MongoDBProperties mongodb;
	}

	@Data
	public static class MongoDBProperties {

		private List<String> hosts;
		private String user;
		private String password;
	}

	@Data
	public static class Database {

		private List<String> whitelist;
		private Collection collection;

		@Data
		public static class Collection {

			private List<String> whitelist;

			private List<String> excludeFieldList;

		}

	}

	@Data
	public static class Kafka {

		private String topicPrefix;

		private boolean collectionNameAsPartitionKey;
	}

	public List<String> obtainDataBaseWhitelist() {

		if (isNull(this.database)) {
			return new ArrayList<>();
		}

		return Optional.ofNullable(this.database.getWhitelist())
			.orElse(new ArrayList<>());

	}

	public List<String> obtainCollectionWhitelist() {

		if (this.isNullDatabaseCollectionObject()) {
			return new ArrayList<>();
		}

		return Optional.ofNullable(this.database.getCollection().getWhitelist())
			.orElse(new ArrayList<>());

	}

	public List<String> obtainExcludeFieldList() {

		if (this.isNullDatabaseCollectionObject()) {
			return new ArrayList<>();
		}

		return Optional.ofNullable(this.database.getCollection().getExcludeFieldList())
			.orElse(new ArrayList<>());

	}

	private boolean isNullDatabaseCollectionObject() {

		return isNull(this.database) || isNull(this.database.getCollection());
	}

}
