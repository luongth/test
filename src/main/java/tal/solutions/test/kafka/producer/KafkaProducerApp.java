package tal.solutions.test.kafka.producer;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import tal.solutions.test.kafka.KafkaConfiguration;
import tal.solutions.test.kafka.SimpleMessagePublisher;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@Import({KafkaConfiguration.class})
public class KafkaProducerApp {

    @Bean
    public String kafkaTopicSimple(@Value("${kafkaSimpleConsumerTopic}") final String kafkaTopic) {
        return kafkaTopic;
    }

    @Bean
    public RateLimiter publishRateLimiter() {
        final RateLimiterConfig publishRateLimiterConfig = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1L))
                .limitForPeriod(15)
                .build();
        return RateLimiter.of("publisherRateLimiter", publishRateLimiterConfig);
    }

    @Bean
    public SimpleMessagePublisher simpleMessagePublisher(final KafkaTemplate<String, String> kafkaTemplate) {
        return new SimpleMessagePublisher(kafkaTemplate);
    }

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(KafkaProducerApp.class, args);
        final SimpleMessagePublisher publisher = context.getBean(SimpleMessagePublisher.class);
        final String kafkaTopic = context.getBean("kafkaTopicSimple", String.class);
        final RateLimiter rateLimiter = context.getBean(RateLimiter.class);

        final AtomicInteger count = new AtomicInteger();
        for (int i=0; i<1000; i++) {
            rateLimiter.executeRunnable(() -> publisher.publish(kafkaTopic, "message number: " + count.incrementAndGet()));
        }
    }

}
