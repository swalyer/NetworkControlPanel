package ru.example.networkmonitoring.model;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "host")
public class Host {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)$")
    @Column(name = "ip_address", nullable = false, unique = true)
    private String ipAddress;

    @Column(name = "description")
    private String description;

    @NotBlank
    @Column(name = "role", nullable = false)
    private String role;

    @NotNull
    @Column(name = "criticality", nullable = false)
    private String criticality;

    @Column(name = "monitored", nullable = false)
    private boolean monitored;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_status", nullable = false)
    private HostStatus lastStatus = HostStatus.UNKNOWN;

    @Column(name = "last_check_time")
    private OffsetDateTime lastCheckTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_segment_id", nullable = false)
    private NetworkSegment networkSegment;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HostCheckResult> checkResults = new ArrayList<>();

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

    public NetworkSegment getNetworkSegment() {
        return networkSegment;
    }

    public void setNetworkSegment(NetworkSegment networkSegment) {
        this.networkSegment = networkSegment;
    }

    public List<HostCheckResult> getCheckResults() {
        return checkResults;
    }

    public void setCheckResults(List<HostCheckResult> checkResults) {
        this.checkResults = checkResults;
    }
}
