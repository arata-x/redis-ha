package dev.aratax.redis.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import dev.aratax.redis.RedisApplication;
import dev.aratax.redis.model.User;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = RedisApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    User sampleUser;

    @BeforeEach
    void setup() {
        sampleUser = new User();
        sampleUser.setId(UUID.randomUUID().toString());
        sampleUser.setName("Integration Test User");
        sampleUser.setEmail("integration@example.com");
    }

    @Test
    void testCreateUser() {
        webTestClient.post()
                .uri("/api/users")
                .body(Mono.just(sampleUser), User.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class);
    }

    @Test
    void testGetUser() {
        webTestClient.post()
                .uri("/api/users")
                .body(Mono.just(sampleUser), User.class)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri("/api/users/{id}", sampleUser.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class);
    }

    @Test
    void testUpdateUser() {
        webTestClient.post()
                .uri("/api/users")
                .body(Mono.just(sampleUser), User.class)
                .exchange()
                .expectStatus().isOk();

        sampleUser.setName("Updated Name");

        webTestClient.put()
                .uri("/api/users/{id}", sampleUser.getId())
                .body(Mono.just(sampleUser), User.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class);
    }

    @Test
    void testDeleteUser() {
        webTestClient.post()
                .uri("/api/users")
                .body(Mono.just(sampleUser), User.class)
                .exchange()
                .expectStatus().isOk();

        webTestClient.delete()
                .uri("/api/users/{id}", sampleUser.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class).isEqualTo(true);
    }

    @Test
    void testUserNotFound() {
        webTestClient.get()
                .uri("/api/users/{id}", "non-existent-id")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteAllUsers() {
        webTestClient.post()
            .uri("/api/users")
            .body(Mono.just(sampleUser), User.class)
            .exchange()
            .expectStatus().isOk();
        
        webTestClient.delete()
            .uri("/api/users")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Boolean.class).isEqualTo(true);
    }

}
