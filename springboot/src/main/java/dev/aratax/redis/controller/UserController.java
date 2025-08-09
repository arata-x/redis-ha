package dev.aratax.redis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import dev.aratax.redis.model.User;

import dev.aratax.redis.service.UserRedisService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST controller for User Redis operations
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users in Redis")
public class UserController {

    private final UserRedisService userRedisService;

    public UserController(UserRedisService userRedisService) {
        this.userRedisService = userRedisService;
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user in Redis. If no ID is provided, a UUID will be generated.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "Invalid user data provided")
    })
    public Mono<User> createUser(@RequestBody @Parameter(description = "User data to create") User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
        return userRedisService.save(user)
                .thenReturn(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user from Redis by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<User> getUser(@PathVariable("id") @Parameter(description = "User ID") String id) {
        return userRedisService.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user in Redis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid user data provided")
    })
    public Mono<User> updateUser(@PathVariable("id") @Parameter(description = "User ID") String id, 
                                @RequestBody @Parameter(description = "Updated user data") User user) {
        user.setId(id);
        user.updateTimestamp();
        return userRedisService.save(user)
                .thenReturn(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user from Redis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deletion status",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<Boolean> deleteUser(@PathVariable("id") @Parameter(description = "User ID") String id) {
        return userRedisService.deleteById(id);
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Check if user exists", description = "Checks whether a user exists in Redis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User existence status",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
    })
    public Mono<Boolean> userExists(@PathVariable("id") @Parameter(description = "User ID") String id) {
        return userRedisService.exists(id);
    }

    @PostMapping("/{id}/expire")
    @Operation(summary = "Set user expiration", description = "Sets an expiration time for a user in Redis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expiration set successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Mono<Boolean> setExpiration(@PathVariable("id") @Parameter(description = "User ID") String id, 
                                      @RequestParam("seconds") @Parameter(description = "Expiration time in seconds") long seconds) {
        return userRedisService.setExpiration(id, seconds);
    }

    @PostMapping("/with-expiration")
    @Operation(summary = "Create user with expiration", description = "Creates a new user in Redis with an expiration time")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User created with expiration successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "Invalid user data or expiration time provided")
    })
    public Mono<User> createUserWithExpiration(@RequestBody @Parameter(description = "User data to create") User user, 
                                              @RequestParam("seconds") @Parameter(description = "Expiration time in seconds") long seconds) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
        return userRedisService.saveWithExpiration(user, seconds)
                .thenReturn(user);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users from Redis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of all users",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    })
    public Flux<User> getAllUsers() {
        return userRedisService.findAll();
    }

    @GetMapping("/count")
    @Operation(summary = "Get user count", description = "Returns the total number of users in Redis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Total user count",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    })
    public Mono<Long> getUserCount() {
        return userRedisService.count();
    }

    @DeleteMapping
    @Operation(summary = "Delete all users", description = "Deletes all users from Redis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All users deleted successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
    })
    public Mono<Boolean> deleteAllUsers() {
        return userRedisService.deleteAll();
    }

}
