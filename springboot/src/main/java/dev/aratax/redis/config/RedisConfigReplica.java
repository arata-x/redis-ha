package dev.aratax.redis.config;

import io.lettuce.core.ReadFrom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import dev.aratax.redis.annotation.ReplicaProfile;

/**
 * Configuration for Master-replica Redis setup
 */
@ReplicaProfile
@Configuration
public class RedisConfigReplica {

    private final RedisConfigProperties properties;

    public RedisConfigReplica(RedisConfigProperties properties) {
        this.properties = properties;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // Master + replicas
        RedisStaticMasterReplicaConfiguration masterConfig = new RedisStaticMasterReplicaConfiguration(
                properties.getMaster().getHost(),
                properties.getMaster().getPort());

        properties.getReplicas()
            .forEach(replica -> 
                masterConfig.node(replica.getHost(), replica.getPort())
                            .setPassword(RedisPassword.of(replica.getPassword()))
            );

        // Read preference
        ReadFrom readFrom = ReadFrom.valueOf(properties.getReadFrom());

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(readFrom)
                .build();

        return new LettuceConnectionFactory(masterConfig, clientConfig);
    }
    
}
