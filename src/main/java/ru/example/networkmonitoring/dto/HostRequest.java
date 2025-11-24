package ru.example.networkmonitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class HostRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)$")
    private String ipAddress;

    private String description;

    @NotBlank
    private String role;

    @NotBlank
    private String criticality;

    @NotNull
    private Boolean monitored;

    @NotNull
    private Long networkSegmentId;

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

    public String getCriticality() {
        return criticality;
    }

    public void setCriticality(String criticality) {
        this.criticality = criticality;
    }

    public Boolean getMonitored() {
        return monitored;
    }

    public void setMonitored(Boolean monitored) {
        this.monitored = monitored;
    }

    public Long getNetworkSegmentId() {
        return networkSegmentId;
    }

    public void setNetworkSegmentId(Long networkSegmentId) {
        this.networkSegmentId = networkSegmentId;
    }
}
