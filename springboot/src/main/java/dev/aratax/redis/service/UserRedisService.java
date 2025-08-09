package dev.aratax.redis.service;

import dev.aratax.redis.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for User Redis operations
 */
public interface UserRedisService {
    
    Mono<Boolean> save(User user);
    
    Mono<User> findById(String id);
    
    Flux<User> findAll();
    
    Mono<Boolean> deleteById(String id);
    
    Mono<Boolean> exists(String id);
    
    Mono<Long> count();
    
    Mono<Boolean> saveWithExpiration(User user, long timeoutInSeconds);
    
    Mono<Boolean> setExpiration(String id, long timeoutInSeconds);

    Mono<Boolean> deleteAll();
}
