# Redis Replication Demo with Spring Boot Reactive

## 📖 Description

### What is this project?
This is a Spring Boot application that demonstrates Redis replication integration using reactive programming patterns. The project showcases how to build scalable, non-blocking applications with Redis as the data store, supporting both standard master-replica and chained-replica deployment configurations.

### Why was this built?
- **Educational Purpose**: Demonstrate best practices for Redis replication in modern Spring Boot applications.
- **Production Readiness**: Show how to handle different replication configurations.
- **Developer Experience**: Provide comprehensive tooling, testing, and documentation for efficient development.
- **Reactive Programming**: Illustrate the benefits of non-blocking, reactive programming with Redis.
- **Scalability**: Demonstrate how to build applications that can scale read operations.

## 📋 Requirements

### System Requirements
- **Java**: Version 21 or higher
- **Maven**: Version 3.6 or higher
- **Docker**: Latest version with Docker Compose support
- **Operating System**: Windows 10+, macOS 10.15+, or Linux (Ubuntu 18.04+)

### Hardware Requirements
- **RAM**: Minimum 4GB, recommended 8GB+
- **Storage**: At least 1GB free space
- **CPU**: Multi-core processor recommended

### Network Requirements
- **Ports**: 8080, 6379-6381 should be available
- **Internet**: Required for Maven dependencies and Docker images

## 🚀 Features

- **Multiple Redis Replication Configurations**: Master-Replica and Chained-Replica
- **Reactive Programming**: Built with Spring WebFlux and reactive Redis templates
- **OpenAPI Documentation**: Complete API documentation with Swagger UI
- **Comprehensive Testing**: Integration tests with request/response logging
- **Management Scripts**: Easy setup, testing, and cleanup utilities

## 📋 Prerequisites

- Java 21+
- Maven 3.6+
- Docker &amp; Docker Compose

## 🚀 Getting Started / Installation

### Step 1: Clone the Repository

```bash
git clone <your-repository-url>
cd <project-directory>
```

### Step 2: Verify Prerequisites

Check that all required tools are installed:

```bash
# Check Java version (should be 21+)
java -version

# Check Maven version (should be 3.6+)
mvn -version

# Check Docker and Docker Compose
docker --version
docker-compose --version
```

### Step 3: Start Redis and the Application

Use the provided script to start the desired Redis Docker environment.

```bash
# This script will prompt you to choose a configuration
./script/redis-startup-docker.sh
```

Once the Redis containers are running, build and run the Spring Boot application:

```bash
cd springboot

# Set the active profile in `src/main/resources/application.yml`
# to match your chosen Docker configuration (e.g., 'replica').

# Clean, compile, and run the application
mvn clean spring-boot:run
```

### Step 4: Verify Installation

1.  **Check Application Health**
    ```bash
    curl http://localhost:8080/actuator/health
    ```

2.  **Test Redis Connection**
    ```bash
    curl http://localhost:8080/api/redis/ping
    ```

3.  **Access Swagger UI**

    Open `http://localhost:8080/swagger-ui.html` in your browser.

### Step 5: Access the Application

-   **Application**: `http://localhost:8080`
-   **Swagger UI**: `http://localhost:8080/swagger-ui.html`
-   **Health Check**: `http://localhost:8080/actuator/health`

## 🔧 Management and Cleanup

### Startup Script (`script/redis-startup-docker.sh`)

An interactive script to start a Redis environment.

```bash
./script/redis-startup-docker.sh
```

**Options:**
- **Master-Replica**: Starts one master and its direct replicas.
- **Chained-Replica**: Starts a master with a replica that has its own replica.

### Cleanup Script (`script/redis-cleanup-docker.sh`)

Use this script to stop and remove the Docker containers.

```bash
./script/redis-cleanup-docker.sh
```

## ❓ FAQ / Troubleshooting

### Common Issues

#### Q: Application fails to start with "Connection refused" error
**A:** Redis is not running or not accessible.

**Solutions:**
1.  Check if Redis containers are running:
    ```bash
    docker ps | grep redis
    ```
2.  Restart the Redis environment using the scripts:
    ```bash
    ./script/redis-cleanup-docker.sh
    ./script/redis-startup-docker.sh
    ```
3.  Check Redis logs for a specific container:
    ```bash
    docker logs <container_name_or_id>
    ```

#### Q: Port 8080 is already in use
**A:** Another application is using port 8080.

**Solutions:**
1.  Stop the conflicting application.
2.  Change the application port:
    ```bash
    cd springboot
    mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
    ```

#### Q: Maven build fails with dependency errors
**A:** Network issues or Maven repository problems.

**Solutions:**
1.  Force update dependencies:
    ```bash
    mvn clean install -U
    ```
2.  Check internet connection and proxy settings.

#### Q: Docker containers fail to start
**A:** Docker daemon issues or port conflicts.

**Solutions:**
1.  Restart the Docker daemon.
2.  Check for conflicting ports (e.g., 6379):
    ```bash
    # On Linux/Mac
    lsof -i:6379
    ```
3.  Clean up the Docker system:
    ```bash
    docker system prune -f
    ```

### Configuration Issues

#### Q: Wrong Redis configuration is being used
**A:** The active Spring profile in your application does not match the running Docker environment.

**Solutions:**
1.  Set the correct profile in `springboot/src/main/resources/application.yml`:
    ```yaml
    spring:
      profiles:
        active: replica # or another relevant profile
    ```
2.  Restart the application.

### Script Issues

#### Q: Scripts show "Permission denied" error
**A:** Scripts don't have execute permissions.

**Solution:**
```bash
chmod +x script/*.sh
```

## 📚 API Documentation

### User Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create a new user |
| GET | `/api/users/{id}` | Get user by ID |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| GET | `/api/users/{id}/exists` | Check if user exists |
| POST | `/api/users/{id}/expire?seconds={n}` | Set user expiration |
| POST | `/api/users/with-expiration?seconds={n}` | Create user with expiration |
| GET | `/api/users` | Get all users |
| GET | `/api/users/count` | Get user count |

### Redis Information Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/redis/ping` | Ping Redis server |
| GET | `/api/redis/config` | Get active Redis configuration profile |

### Example User Object

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "age": 30,
  "createdAt": "2023-12-01T10:30:00",
  "updatedAt": "2023-12-01T15:45:00"
}
```

## 🧪 Testing

Run the complete integration test suite:

```bash
cd springboot
mvn test
```
*Note: The tests may require a running Redis instance that matches the default `replica` profile.*

## 🐳 Docker Configurations

### Available Configurations

1.  **Master-Replica** (`docker/redis-replica/`)
    -   1 Master + 2 replicas.
    -   Ports: 6379 (master), 6380-6381 (replicas).
    -   Best for: Read scaling and basic redundancy.

2.  **Chained-Replica** (`docker/redis-replica-chained/`)
    -   1 Master -> 1 Replica -> 1 Chained Replica.
    -   Demonstrates cascading replication.
    -   Best for: Advanced replication scenarios.

## 🔧 Configuration

### Application Profiles

Configure the Redis connection by setting the active Spring profile in `springboot/src/main/resources/application.yml`:

```yaml
spring:
  profiles:
    active: replica  # This should match the desired Redis setup
```

The application automatically configures its Redis connection based on the active profile.

## 📖 OpenAPI Integration

### Accessing Documentation

-   **Swagger UI**: `http://localhost:8080/swagger-ui.html`
-   **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
-   **OpenAPI YAML**: `http://localhost:8080/v3/api-docs.yaml`

## 📄 License

-   Code is licensed under [Apache License 2.0](./LICENSE).
-   Documentation and assets are licensed under [CC-BY 4.0](./LICENSE-DOCS).
