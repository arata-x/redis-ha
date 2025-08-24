package dev.aratax.redis.config;

import java.util.List;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.aratax.redis.annotation.SentinelProfile;
import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@SentinelProfile
@Configuration
public class RedisConfigSentinel {

  @Bean(destroyMethod = "shutdown")
  public RedisClient sentinelClient(RedisProperties redisProperties) {
    List<String> sentinelNodes = redisProperties.getSentinel().getNodes();
    if (sentinelNodes.isEmpty()) {
      throw new IllegalStateException("No sentinel nodes configured in spring.data.redis.sentinel.nodes");
    }
    // point at one of your Sentinel nodes (port 26379)
    return RedisClient.create(String.format("redis://%s", sentinelNodes.get(0)));
  }

  @Bean(destroyMethod = "close")
  public StatefulRedisPubSubConnection<String, String> sentinelPubSub(RedisClient client) {
    var conn = client.connectPubSub();                 // connect to Sentinel (Redis protocol)
    conn.addListener(new RedisPubSubAdapter<>() {
      @Override public void message(String channel, String message) {
        log.info("Sentinel event [{}] {}", channel, message);
        // TODO: react to events: refresh caches, notify ops, export metrics, etc.
      }
    });

    // subscribe to key Sentinel events (or use psubscribe("*") to get all)
    conn.sync().subscribe(
        "+switch-master",        // master changed
        "+sdown", "-sdown",      // subjective down / cleared
        "+odown", "-odown",      // objective down / cleared (masters only)
        "+try-failover",
        "+failover-state-*"
    );
    return conn;
  }

}
