package ru.example.networkmonitoring.dto;

public class NetworkSegmentResponse {

    private Long id;
    private String name;
    private String description;
    private String networkCidr;
    private String location;
    private long hostsCount;

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

    public long getHostsCount() {
        return hostsCount;
    }

    public void setHostsCount(long hostsCount) {
        this.hostsCount = hostsCount;
    }
}
