package dev.aratax.redis.service.impl;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

import dev.aratax.redis.service.impl.RedisInfoServiceImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class RedisInfoServiceImplTest {

    @Mock
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Mock
    private ReactiveRedisConnectionFactory connectionFactory;

    @Mock
    private ReactiveRedisConnection connection;

    private RedisInfoServiceImpl redisInfoService;

    @BeforeEach
    void setup() {
        redisInfoService = new RedisInfoServiceImpl(reactiveRedisTemplate);
    }

    @Test
    void testPing() {
        // Given
        when(reactiveRedisTemplate.getConnectionFactory()).thenReturn(connectionFactory);
        when(connectionFactory.getReactiveConnection()).thenReturn(connection);
        when(connection.ping()).thenReturn(Mono.just("PONG"));

        // When & Then
        StepVerifier.create(redisInfoService.ping())
                .expectNext("PONG")
                .verifyComplete();
    }

}
