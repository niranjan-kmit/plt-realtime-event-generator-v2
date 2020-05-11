package org.kafka.loadgenerator;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.kafka.loadgenerator.service.DynamicScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@SpringBootApplication
public class LoadGenerateApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadGenerateApplication.class, args);
	}

	@Autowired
	private KafkaProperties kafkaProperties;

	@Value("${bootstrap-servers}")
    private String bootstrapServers;
	
	@Value("${kafka.topic-name}")
	private String topicName;
	
	@Value("${event.autogenerate}")
	private boolean eventEnabled;

	// Producer configuration

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return props;
	}

	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public NewTopic adviceTopic() {
		return new NewTopic(topicName, 3, (short) 1);
	}
	
	@Bean
	public ScheduledTaskRegistrar scheduledTaskRegistrar() {
		return new ScheduledTaskRegistrar();
	}
	
	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		final JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
		jsonDeserializer.addTrustedPackages("*");
		return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(), new StringDeserializer(),
				jsonDeserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());

		return factory;
	}
	
	@Bean
    @ConditionalOnProperty(value = "jobs.enabled", matchIfMissing = false, havingValue = "true")
	public DynamicScheduler scheduledJob() {
		
		DynamicScheduler dynamicScheduler = new DynamicScheduler();
		
		return dynamicScheduler;
	}
	

}