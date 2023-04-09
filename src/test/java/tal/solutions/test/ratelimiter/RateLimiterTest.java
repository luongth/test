package tal.solutions.test.ratelimiter;


import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class RateLimiterTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiterTest.class);

    @Test
    public void verify_request_blocked_when_limit_exceeded() {
        // Given
        final AtomicInteger count = new AtomicInteger();
        final RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(1)
                .limitRefreshPeriod(Duration.ofSeconds(10L))
                .build();
        final RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
        final RateLimiter limiter = rateLimiterRegistry.rateLimiter("limiter");
        final CheckedRunnable restrictedRequest = RateLimiter.decorateCheckedRunnable(limiter, count::incrementAndGet);
        // When
        Try.run(restrictedRequest)
                .andThenTry(restrictedRequest).onFailure(t -> LOGGER.info("Call not permitted at this time"));

        // Then
        assertThat(count.get()).isEqualTo(1);

        // When

    }

}
