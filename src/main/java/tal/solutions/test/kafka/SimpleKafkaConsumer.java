package tal.solutions.test.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Objects;

public class SimpleKafkaConsumer implements KafkaConsumerSeekAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleKafkaConsumer.class);

    private final String kafkaTopic;

    public SimpleKafkaConsumer(String kafkaTopic) {
        this.kafkaTopic = Objects.requireNonNull(kafkaTopic);
    }

    @KafkaListener(id="${kafkaSimpleConsumerId}",
                   topics="${kafkaSimpleConsumerTopic}",
                   containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(@Payload String payload) {
        LOGGER.info("Received: " + payload);
    }

    @Override
    public String kafkaTopic() {
        return kafkaTopic;
    }
}
