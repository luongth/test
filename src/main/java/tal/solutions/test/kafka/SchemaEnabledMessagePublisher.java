package tal.solutions.test.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import tal.solutions.test.avro.AvroSimpleMessage;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class SchemaEnabledMessagePublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaEnabledMessagePublisher.class);

    private final KafkaTemplate<String, AvroSimpleMessage> kafkaTemplate;

    public SchemaEnabledMessagePublisher(KafkaTemplate<String, AvroSimpleMessage> kafkaTemplate) {
        this.kafkaTemplate = Objects.requireNonNull(kafkaTemplate);;
    }

    public void publish(final String topic, final String message) {
        final AvroSimpleMessage payload = AvroSimpleMessage.newBuilder().setPayload(message).setMessageType("simple").build();
        final CompletableFuture<SendResult<String, AvroSimpleMessage>> future =
                kafkaTemplate.send(topic, payload);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logSuccess(payload, result);
            } else {
                logFailure(payload, topic, ex);
            }
        });
    }

    private void logSuccess(final AvroSimpleMessage payload, final SendResult<String, AvroSimpleMessage> result) {
        LOGGER.info("Sent message='{}', topic='{}', partition='{}', offset='{}'",
                payload,
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset());
    }

    private void logFailure(final AvroSimpleMessage payload, final String topic, final Throwable exception) {
        LOGGER.error("Unable to send message='{}', topic='{}' due to: '{}'",
                payload,
                topic,
                exception.getMessage());
    }

}