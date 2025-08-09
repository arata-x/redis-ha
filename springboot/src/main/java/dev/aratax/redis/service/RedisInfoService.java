package dev.aratax.redis.service;

import reactor.core.publisher.Mono;

/**
 * Service interface for Redis information operations
 */
public interface RedisInfoService {
    
    /**
     * Ping Redis server to check connectivity
     * @return Mono containing ping response (usually "PONG")
     */
    Mono<String> ping();

}
