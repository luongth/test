package tal.solutions.test.kafka.schema.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import tal.solutions.test.kafka.KafkaConfiguration;
import tal.solutions.test.kafka.SchemaEnabledKafkaConsumer;

@EnableKafka
@SpringBootApplication
@Import({KafkaConfiguration.class})
public class KafkaSchemaRegistryConsumerApp {

    public static void main(String[] args) {
        System.setProperty("schema.registry.enabled", "true");
        SpringApplication.run(KafkaSchemaRegistryConsumerApp.class, args);
    }

    @Bean
    public SchemaEnabledKafkaConsumer schemaEnabledKafkaConsumer(@Value("${kafkaSchemaEnabledConsumerTopic}") final String kafkaSchemaEnabledConsumerTopic) {
        return new SchemaEnabledKafkaConsumer(kafkaSchemaEnabledConsumerTopic);
    }
}
