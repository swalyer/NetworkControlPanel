package ru.example.networkmonitoring.dto;

import jakarta.validation.constraints.NotBlank;

public class NetworkSegmentRequest {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String networkCidr;

    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNetworkCidr() {
        return networkCidr;
    }

    public void setNetworkCidr(String networkCidr) {
        this.networkCidr = networkCidr;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
