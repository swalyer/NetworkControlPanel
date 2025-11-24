# Network Monitoring Service

Educational RESTful service built with Spring Boot 3 and Java 17 that stores corporate network hosts and checks their availability. The project is intentionally simple, using in-memory storage to focus on API design, validation, and monitoring concepts for coursework.

## Features
- Manage hosts with basic attributes and validation.
- Trigger availability checks in simulation or real reachability mode.
- Exposes health endpoint via Spring Boot Actuator.
- Ready-to-run with Maven, Docker, and optional docker-compose.

## Project layout
```
network-monitoring-service/
├─ README.md
├─ pom.xml
├─ docker/
│  └─ Dockerfile
├─ docker-compose.yml
├─ config/
│  └─ application.yml
├─ src/
│  ├─ main/java/ru/example/networkmonitoring
│  │  ├─ NetworkMonitoringApplication.java
│  │  ├─ controller/HostController.java
│  │  ├─ exception/…
│  │  ├─ model/…
│  │  ├─ repository/InMemoryHostRepository.java
│  │  └─ service/MonitoringService.java
│  └─ test/java/ru/example/networkmonitoring
│     ├─ HostControllerIntegrationTests.java
│     └─ MonitoringServiceTests.java
└─ src/main/resources/application.yml
```

## Building and running
### Prerequisites
- Java 17+
- Maven 3.9+

### Run locally
```bash
mvn spring-boot:run
```
The API is available at `http://localhost:8080`.

### Configuration
Key properties (see `src/main/resources/application.yml`):
- `monitoring.simulation-mode`: when `true`, derives deterministic statuses from the IP address instead of performing network IO.
- `monitoring.timeout-millis`: timeout used for reachability checks when simulation is disabled.

### Docker
Build and run the container:
```bash
docker build -f docker/Dockerfile -t network-monitoring-service .
docker run -p 8080:8080 network-monitoring-service
```

Or use docker-compose:
```bash
docker-compose up --build
```

## API overview
Base path: `/api/hosts`

- `GET /api/hosts` — list all hosts.
- `POST /api/hosts` — create a host (fields: `name`, `ipAddress`, `role`, optional `description`).
- `GET /api/hosts/{id}` — fetch a host by id.
- `DELETE /api/hosts/{id}` — remove a host.
- `POST /api/hosts/{id}/check` — perform an availability check and return a `HostCheckResult`.

Errors are returned as structured JSON with `timestamp`, `status`, `error`, `message`, and `path`.

Health check endpoint: `GET /actuator/health`.

## Testing
Run unit and integration tests:
```bash
mvn test
```
