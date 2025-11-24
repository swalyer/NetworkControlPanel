package ru.example.networkmonitoring.dto;

import java.time.OffsetDateTime;

public class HostResponse {

    private Long id;
    private String name;
    private String ipAddress;
    private String description;
    private String role;
    private String criticality;
    private boolean monitored;
    private String lastStatus;
    private OffsetDateTime lastCheckTime;
    private Long networkSegmentId;
    private String networkSegmentName;

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

    public String getCriticality() {
        return criticality;
    }

    public void setCriticality(String criticality) {
        this.criticality = criticality;
    }

    public boolean isMonitored() {
        return monitored;
    }

    public void setMonitored(boolean monitored) {
        this.monitored = monitored;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public OffsetDateTime getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(OffsetDateTime lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public Long getNetworkSegmentId() {
        return networkSegmentId;
    }

    public void setNetworkSegmentId(Long networkSegmentId) {
        this.networkSegmentId = networkSegmentId;
    }

    public String getNetworkSegmentName() {
        return networkSegmentName;
    }

    public void setNetworkSegmentName(String networkSegmentName) {
        this.networkSegmentName = networkSegmentName;
    }
}
