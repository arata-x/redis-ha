package dev.aratax.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Base Redis Configuration for Reactive Redis Operations
 * This configuration provides common beans for all Redis setups
 */
@Configuration
public class RedisConfig {

    /**
     * Configure ObjectMapper specifically for Redis serialization with JSR310
     * support and type information
     */
    @Bean("redisObjectMapper")
    public ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Enable type information to avoid ClassCastException during deserialization
        objectMapper.activateDefaultTyping(
            objectMapper.getPolymorphicTypeValidator(),
            com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL
        );
        return objectMapper;
    }

    /**
     * Configure default ObjectMapper for REST API responses with JSR310 support
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    /**
     * Configure ReactiveRedisTemplate with JSON serialization
     * This template will be used for all reactive Redis operations
     */
    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory) {
        
        ObjectMapper redisObjectMapper = redisObjectMapper();
        
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer(redisObjectMapper);
        
        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        
        RedisSerializationContext<String, Object> context = builder
                .key(keySerializer)
                .hashKey(keySerializer)
                .value(valueSerializer)
                .hashValue(valueSerializer)
                .build();
        
        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }

    /**
     * Configure ReactiveRedisTemplate for String operations
     * Optimized for simple string key-value operations
     */
    @Bean
    public ReactiveRedisTemplate<String, String> reactiveStringRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory) {
        
        StringRedisSerializer serializer = new StringRedisSerializer();
        
        RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder =
                RedisSerializationContext.newSerializationContext(serializer);
        
        RedisSerializationContext<String, String> context = builder
                .key(serializer)
                .hashKey(serializer)
                .value(serializer)
                .hashValue(serializer)
                .build();
        
        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}
