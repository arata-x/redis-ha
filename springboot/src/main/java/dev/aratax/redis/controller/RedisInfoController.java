package dev.aratax.redis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.aratax.redis.service.RedisInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

/**
 * Controller to provide Redis configuration information
 */
@RestController
@RequestMapping("/api/redis")
@Tag(name = "Redis Information", description = "APIs for Redis server information and health checks")
public class RedisInfoController {

    private final RedisInfoService redisInfoService;

    public RedisInfoController(RedisInfoService redisInfoService) {
        this.redisInfoService = redisInfoService;
    }

    @GetMapping("/ping")
    @Operation(summary = "Ping Redis server", description = "Checks if Redis server is responding by sending a PING command")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Redis server is responding",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class, example = "PONG"))),
        @ApiResponse(responseCode = "500", description = "Redis server is not responding")
    })
    public Mono<String> ping() {
        return redisInfoService.ping();
    }

}
