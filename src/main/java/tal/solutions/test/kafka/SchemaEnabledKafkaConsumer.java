package tal.solutions.test.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import tal.solutions.test.avro.AvroSimpleMessage;

public class SchemaEnabledKafkaConsumer implements KafkaConsumerSeekAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaEnabledKafkaConsumer.class);

    private final String kafkaTopic;

    public SchemaEnabledKafkaConsumer(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    @Override
    public String kafkaTopic() {
        return kafkaTopic;
    }

    @KafkaListener(id="${kafkaSchemaEnabledConsumerId}",
            topics="${kafkaSchemaEnabledConsumerTopic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(@Payload AvroSimpleMessage payload) {
        LOGGER.info("Received: " + payload);
    }
}
