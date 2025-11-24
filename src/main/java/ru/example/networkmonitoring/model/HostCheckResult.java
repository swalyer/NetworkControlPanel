package ru.example.networkmonitoring.model;

import java.time.OffsetDateTime;

public class HostCheckResult {

    private Long id;
    private Long hostId;
    private OffsetDateTime timestamp;
    private HostStatus status;
    private Long responseTimeMillis;
    private String message;

    public HostCheckResult() {
    }

    public HostCheckResult(Long id, Long hostId, OffsetDateTime timestamp, HostStatus status,
                           Long responseTimeMillis, String message) {
        this.id = id;
        this.hostId = hostId;
        this.timestamp = timestamp;
        this.status = status;
        this.responseTimeMillis = responseTimeMillis;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public HostStatus getStatus() {
        return status;
    }

    public void setStatus(HostStatus status) {
        this.status = status;
    }

    public Long getResponseTimeMillis() {
        return responseTimeMillis;
    }

    public void setResponseTimeMillis(Long responseTimeMillis) {
        this.responseTimeMillis = responseTimeMillis;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
