package tal.solutions.test.kafka;

import org.springframework.kafka.core.KafkaTemplate;

import java.util.Objects;

public class SimpleMessagePublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public SimpleMessagePublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = Objects.requireNonNull(kafkaTemplate);
    }

    public void publish(final String topic, final String message) {
        kafkaTemplate.send(topic, message);
    }
}
