package dev.aratax.redis.service.impl;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import dev.aratax.redis.model.User;
import dev.aratax.redis.service.UserRedisService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** Implementation of UserRedisService */
@Service
@RequiredArgsConstructor
public class UserRedisServiceImpl implements UserRedisService {

  private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;
  private static final String USER_KEY_PREFIX = "user::";

  @Override
  public Mono<Boolean> save(User user) {
    String key = USER_KEY_PREFIX + user.getId();
    return reactiveRedisTemplate.opsForValue().set(key, user);
  }

  @Override
  public Mono<User> findById(String id) {
    String key = USER_KEY_PREFIX + id;
    return reactiveRedisTemplate.opsForValue().get(key).cast(User.class);
  }

  @Override
  public Flux<User> findAll() {
    return reactiveRedisTemplate
        .keys(USER_KEY_PREFIX + "*")
        .flatMap(key -> reactiveRedisTemplate.opsForValue().get(key))
        .cast(User.class);
  }

  @Override
  public Mono<Boolean> deleteById(String id) {
    String key = USER_KEY_PREFIX + id;
    return reactiveRedisTemplate.delete(key).map(count -> count > 0);
  }

  @Override
  public Mono<Boolean> exists(String id) {
    String key = USER_KEY_PREFIX + id;
    return reactiveRedisTemplate.hasKey(key);
  }

  @Override
  public Mono<Long> count() {
    return reactiveRedisTemplate.keys(USER_KEY_PREFIX + "*").count();
  }

  @Override
  public Mono<Boolean> saveWithExpiration(User user, long timeoutInSeconds) {
    String key = USER_KEY_PREFIX + user.getId();
    return reactiveRedisTemplate.opsForValue().set(key, user, Duration.ofSeconds(timeoutInSeconds));
  }

  @Override
  public Mono<Boolean> setExpiration(String id, long timeoutInSeconds) {
    String key = USER_KEY_PREFIX + id;
    return reactiveRedisTemplate.expire(key, Duration.ofSeconds(timeoutInSeconds));
  }

  @Override
  public Mono<Boolean> deleteAll() {
    return reactiveRedisTemplate
        .keys(USER_KEY_PREFIX + "*")                     
        .flatMap(reactiveRedisTemplate::delete)          
        .reduce(0L, Long::sum)                           
        .map(count -> count > 0)                         
        .switchIfEmpty(Mono.just(false));
  }
}
