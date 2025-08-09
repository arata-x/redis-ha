package dev.aratax.redis.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User model for Redis operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User entity stored in Redis")
public class User {
    
    @Schema(description = "Unique identifier for the user", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;
    
    @Schema(description = "User's full name", example = "John Doe", required = true)
    private String name;
    
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
    private String email;
    
    @Schema(description = "User's age", example = "30", minimum = "0", maximum = "150")
    private Integer age;
    
    @Schema(description = "Timestamp when the user was created", example = "2023-12-01T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the user was last updated", example = "2023-12-01T15:45:00")
    private LocalDateTime updatedAt;

    public User(String id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
