package com.eduservices.campusmgmt.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for creating Kafka topics programmatically on application
 * startup
 */
@Configuration
public class KafkaTopicConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic userCreatedTopic() {
		// Create topic with 3 partitions and replication factor 1
		return new NewTopic("user.created", 3, (short) 1);
	}

	@Bean
	public NewTopic userUpdatedTopic() {
		// Create topic with 3 partitions and replication factor 1
		return new NewTopic("user.updated", 3, (short) 1);
	}

	@Bean
	public NewTopic userDeletedTopic() {
		// Create topic with 3 partitions and replication factor 1
		return new NewTopic("user.deleted", 3, (short) 1);
	}
}
