package tal.solutions.test.kafka;

import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerSeekAware;

import java.util.Map;

/**
 * SeekToEnd on partitions assigned
 */
public interface KafkaConsumerSeekAware extends ConsumerSeekAware {

    String kafkaTopic();

    @Override
    default void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        ConsumerSeekAware.super.onPartitionsAssigned(assignments, callback);
        assignments.keySet().stream()
                .filter(p -> kafkaTopic().equals(p.topic()))
                .forEach(p -> callback.seekToEnd(kafkaTopic(), p.partition()));
    }
}
