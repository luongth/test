package tal.solutions.test.kafka.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import tal.solutions.test.kafka.KafkaConfiguration;
import tal.solutions.test.kafka.SimpleKafkaConsumer;

@EnableKafka
@SpringBootApplication
@Import({KafkaConfiguration.class})
public class KafkaConsumerApp {

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerApp.class, args);
    }

    @Bean
    public SimpleKafkaConsumer simpleKafkaConsumer(@Value("${kafkaSimpleConsumerTopic}") final String kafkaSimpleConsumerTopic) {
        return new SimpleKafkaConsumer(kafkaSimpleConsumerTopic);
    }
}
