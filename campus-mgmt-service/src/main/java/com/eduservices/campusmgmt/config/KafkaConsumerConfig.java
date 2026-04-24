package com.eduservices.campusmgmt.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.eduservices.profile.dto.PersonalDetailsDto;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Value("${spring.kafka.consumer.auto-offset-reset}")
	private String autoOffsetReset;

	@Value("${spring.kafka.consumer.enable-auto-commit}")
	private boolean enableAutoCommit;

	@Value("${spring.kafka.properties.spring.json.trusted.packages}")
	private String trustedPackages;

	@Value("${spring.kafka.properties.spring.json.type.mapping}")
	private String typePackages;

	@Bean
	public ConsumerFactory<String, PersonalDetailsDto> userConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		// Trust packages configuration
		props.put(JsonDeserializer.TRUSTED_PACKAGES, trustedPackages);
		props.put(JsonDeserializer.TYPE_MAPPINGS, typePackages);

		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
				new JsonDeserializer<>(PersonalDetailsDto.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PersonalDetailsDto> userKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, PersonalDetailsDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(userConsumerFactory());
		return factory;
	}
}
