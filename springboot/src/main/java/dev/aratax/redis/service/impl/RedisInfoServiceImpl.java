package dev.aratax.redis.service.impl;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import dev.aratax.redis.service.RedisInfoService;
import reactor.core.publisher.Mono;

/**
 * Implementation of RedisInfoService for Redis information operations
 */
@Service
public class RedisInfoServiceImpl implements RedisInfoService {

    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public RedisInfoServiceImpl(ReactiveRedisTemplate<String, Object> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Mono<String> ping() {
        return reactiveRedisTemplate.getConnectionFactory()
                .getReactiveConnection()
                .ping();
    }

}
