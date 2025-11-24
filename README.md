# Network Monitoring Service

Educational Spring Boot 3 service used in university coursework on corporate computer networks. The application models network segments, hosts, and availability check results, persists them in PostgreSQL via JPA, and exposes REST endpoints with scheduled monitoring and metrics.

## Features
- Manage network segments and hosts with validation and relations.
- Trigger one-time availability checks for a host.
- Scheduled background checks for monitored hosts.
- Metrics via Spring Boot Actuator and Micrometer.
- Flyway migrations with demo data.

## Technology stack
- Java 17
- Spring Boot 3
- Spring Data JPA (Hibernate)
- PostgreSQL (H2 with Flyway for tests)
- Flyway migrations
- Spring Boot Actuator + Micrometer
- Maven

## Running locally
### Prerequisites
- Java 17+
- Maven 3.9+
- PostgreSQL running on `localhost:5432` with database `network_monitoring` and user/password `network_monitoring` (or adjust `spring.datasource` in `src/main/resources/application.yml`).

### Steps
```bash
mvn clean package
mvn spring-boot:run
```

The API is available at `http://localhost:8080`.

## Docker
Use Docker Compose to start PostgreSQL and the service together:
```bash
docker compose up --build
```

The compose file provisions PostgreSQL with the same credentials as in `application.yml`, runs Flyway migrations automatically, and exposes the service on port 8080.

## Configuration
Key settings in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/network_monitoring
    username: network_monitoring
    password: network_monitoring
  jpa:
    hibernate:
      ddl-auto: validate
flyway:
  enabled: true
monitoring:
  simulation-mode: true
  timeout-millis: 1000
  scheduling:
    enabled: true
    fixed-delay-millis: 60000
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

Set `monitoring.simulation-mode` to `false` for real reachability checks using ICMP with the configured timeout.

## REST API
- `GET /api/segments` — list network segments.
- `POST /api/segments` — create a segment.
- `GET /api/segments/{id}` — get segment details.
- `DELETE /api/segments/{id}` — remove a segment without hosts.
- `GET /api/segments/{id}/hosts` — list hosts in a segment.

- `GET /api/hosts` — list all hosts.
- `POST /api/hosts` — create a host.
- `GET /api/hosts/{id}` — get host details.
- `PUT /api/hosts/{id}` — update host.
- `DELETE /api/hosts/{id}` — delete host.
- `POST /api/hosts/{id}/check` — perform a one-time availability check.
- `GET /api/hosts/{id}/history` — latest availability check results.

Errors are returned as JSON with `timestamp`, `status`, `error`, `message`, and `path`.

### Example cURL commands
Create a segment:
```bash
curl -X POST http://localhost:8080/api/segments \
  -H 'Content-Type: application/json' \
  -d '{"name":"Office LAN","networkCidr":"192.168.10.0/24","location":"HQ","description":"Main office"}'
```

Create a host in that segment (replace `SEGMENT_ID`):
```bash
curl -X POST http://localhost:8080/api/hosts \
  -H 'Content-Type: application/json' \
  -d '{"name":"Gateway","ipAddress":"192.168.10.1","role":"gateway","criticality":"HIGH","monitored":true,"networkSegmentId":SEGMENT_ID}'
```

List hosts:
```bash
curl http://localhost:8080/api/hosts
```

Check a host:
```bash
curl -X POST http://localhost:8080/api/hosts/1/check
```

View host history:
```bash
curl http://localhost:8080/api/hosts/1/history
```

## Actuator
Health and metrics endpoints are exposed at `/actuator/health` and `/actuator/metrics`. Custom counters track total checks and failed checks.

## Testing
Tests use H2 with Flyway migrations:
```bash
mvn test
```
