package dev.aratax.redis.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Configuration properties for Redis setup information
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisConfigProperties {
    
    private Node master;
    private List<Node> replicas;
    private String readFrom; 
    private LettucePool lettuce = new LettucePool();

    @Data
    public static class Node {
        private String host;
        private int port;
        private String password;
    }

    @Data
    public static class LettucePool {
        private int maxActive;
        private int maxIdle;
        private int minIdle;
    }

}
