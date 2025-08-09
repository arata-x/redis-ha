package dev.aratax.redis.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import dev.aratax.redis.RedisApplication;

@SpringBootTest(classes = RedisApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisInfoControllerIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testPing() {
        webTestClient.get()
                .uri("/api/redis/ping")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    System.out.println("Request: GET /api/redis/ping");
                    System.out.println("Response Status: " + response.getStatus());
                    System.out.println("Response Body: " + response.getResponseBody());
                })
                .isEqualTo("PONG");
    }
    
}
