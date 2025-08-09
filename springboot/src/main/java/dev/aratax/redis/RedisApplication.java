package dev.aratax.redis;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * Main Spring Boot Application for Redis Reactive Demo
 * 
 * This application demonstrates Redis reactive operations with different configurations:
 * - Master-replica Redis
 * - Redis Sentinel
 * - Redis Cluster
 * 
 * Use different profiles to switch between configurations:
 * --spring.profiles.active=replica
 * --spring.profiles.active=sentinel
 * --spring.profiles.active=cluster
 */
@SpringBootApplication
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
