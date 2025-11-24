package ru.example.networkmonitoring.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.OffsetDateTime;

public class Host {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "IP address is required")
    @Pattern(
            regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)$",
            message = "IP address must be a valid IPv4 address"
    )
    private String ipAddress;

    private String description;

    @NotBlank(message = "Role is required")
    private String role;

    private HostStatus lastStatus = HostStatus.UNKNOWN;

    private OffsetDateTime lastCheckTime;

    public Host() {
    }

    public Host(Long id, String name, String ipAddress, String description, String role) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.description = description;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public HostStatus getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(HostStatus lastStatus) {
        this.lastStatus = lastStatus;
    }

    public OffsetDateTime getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(OffsetDateTime lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }
}
