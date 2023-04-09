package tal.solutions.test.kafka;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import tal.solutions.test.avro.AvroSimpleMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    private static Map<String, Object> producerConfig(final Environment environment) {
        final Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, environment.getProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, environment.getProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
        return config;
    }

    private static Map<String, Object> consumerConfig(final Environment environment) {
        final Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, environment.getProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, environment.getProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
        config.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, environment.getProperty(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS));
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, environment.getProperty(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS));
        return config;
    }

    @Configuration
    @ConditionalOnProperty(name = "schema.registry.enabled", havingValue = "true")
    @EncryptablePropertySource("kafka/kafka-schema-registry.properties")
    public static class SchemaRegistryEnabledConfig {

        private static Map<String, Object> schemaRegistryConfig(final Map<String, Object> coreConfig, final Environment environment) {
            coreConfig.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, environment.getProperty(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG));
            return coreConfig;
        }

        @Bean
        public ProducerFactory<String, AvroSimpleMessage> producerFactory(final Environment environment) {
            final Map<String, Object> config = schemaRegistryConfig(producerConfig(environment), environment);
            return new DefaultKafkaProducerFactory<>(config);
        }

        @Bean
        public KafkaTemplate<String, AvroSimpleMessage> kafkaTemplate(final ProducerFactory<String, AvroSimpleMessage> producerFactory) {
            return new KafkaTemplate<>(producerFactory);
        }

        @Bean
        public ConsumerFactory<String, AvroSimpleMessage> consumerFactory(final Environment environment) {
            final Map<String, Object> config = schemaRegistryConfig(consumerConfig(environment), environment);
            config.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, environment.getProperty(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG));
            return new DefaultKafkaConsumerFactory<>(config);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, AvroSimpleMessage> kafkaListenerContainerFactory(final ConsumerFactory<String, AvroSimpleMessage> consumerFactory) {
            final ConcurrentKafkaListenerContainerFactory<String, AvroSimpleMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory);
            return factory;
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "schema.registry.enabled", havingValue = "false", matchIfMissing = true)
    @EncryptablePropertySource("kafka/kafka.properties")
    public static class BasicConfig {

        @Bean
        public ProducerFactory<String, String> producerFactory(final Environment environment) {
            final Map<String, Object> config = producerConfig(environment);
             return new DefaultKafkaProducerFactory<>(config);
        }

        @Bean
        public KafkaTemplate<String, String> kafkaTemplate(final ProducerFactory<String, String> producerFactory) {
            return new KafkaTemplate<>(producerFactory);
        }

        @Bean
        public ConsumerFactory<String, String> consumerFactory(final Environment environment) {
            final Map<String, Object> config = consumerConfig(environment);
            return new DefaultKafkaConsumerFactory<>(config);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(final ConsumerFactory<String, String> consumerFactory) {
            final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory);
            return factory;
        }

    }
}
