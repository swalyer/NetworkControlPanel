package ru.example.networkmonitoring.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.networkmonitoring.config.MonitoringProperties;
import ru.example.networkmonitoring.dto.HostCheckResultResponse;
import ru.example.networkmonitoring.exception.NotFoundException;
import ru.example.networkmonitoring.model.Host;
import ru.example.networkmonitoring.model.HostCheckResult;
import ru.example.networkmonitoring.model.HostStatus;
import ru.example.networkmonitoring.repository.HostCheckResultRepository;
import ru.example.networkmonitoring.repository.HostRepository;

@Service
@Transactional
public class MonitoringService {

    private final HostRepository hostRepository;
    private final HostCheckResultRepository hostCheckResultRepository;
    private final MonitoringProperties monitoringProperties;
    private final Counter totalChecksCounter;
    private final Counter failedChecksCounter;

    public MonitoringService(HostRepository hostRepository,
                             HostCheckResultRepository hostCheckResultRepository,
                             MonitoringProperties monitoringProperties,
                             MeterRegistry meterRegistry) {
        this.hostRepository = hostRepository;
        this.hostCheckResultRepository = hostCheckResultRepository;
        this.monitoringProperties = monitoringProperties;
        this.totalChecksCounter = meterRegistry.counter("monitoring.checks.total");
        this.failedChecksCounter = meterRegistry.counter("monitoring.checks.failed");
    }

    public HostCheckResultResponse checkHostAvailability(Long hostId) {
        Host host = findHost(hostId);
        OffsetDateTime timestamp = OffsetDateTime.now();
        HostStatus status;
        Long responseTimeMillis = null;
        String errorMessage = null;
        totalChecksCounter.increment();
        if (monitoringProperties.isSimulationMode()) {
            status = simulateStatus(host.getIpAddress());
            if (status != HostStatus.DOWN) {
                responseTimeMillis = calculateSimulatedLatency(host.getIpAddress());
            } else {
                errorMessage = "Simulated unreachable host";
            }
        } else {
            long start = System.nanoTime();
            try {
                InetAddress address = InetAddress.getByName(host.getIpAddress());
                boolean reachable = address.isReachable(monitoringProperties.getTimeoutMillis());
                long elapsed = Duration.ofNanos(System.nanoTime() - start).toMillis();
                if (reachable) {
                    status = HostStatus.UP;
                    responseTimeMillis = elapsed;
                } else {
                    status = HostStatus.DOWN;
                    errorMessage = "Host did not respond";
                }
            } catch (IOException ex) {
                status = HostStatus.DOWN;
                errorMessage = ex.getMessage();
            }
        }
        if (status == HostStatus.DOWN || status == HostStatus.DEGRADED) {
            failedChecksCounter.increment();
        }
        host.setLastStatus(status);
        host.setLastCheckTime(timestamp);
        hostRepository.save(host);
        HostCheckResult checkResult = new HostCheckResult();
        checkResult.setHost(host);
        checkResult.setTimestamp(timestamp);
        checkResult.setStatus(status);
        checkResult.setResponseTimeMillis(responseTimeMillis);
        checkResult.setErrorMessage(errorMessage);
        HostCheckResult saved = hostCheckResultRepository.save(checkResult);
        return toResponse(saved);
    }

    public void checkAllMonitoredHosts() {
        List<Host> hosts = hostRepository.findByMonitoredTrue();
        for (Host host : hosts) {
            checkHostAvailability(host.getId());
        }
    }

    public List<HostCheckResultResponse> getRecentResults(Long hostId) {
        findHost(hostId);
        return hostCheckResultRepository.findTop10ByHostIdOrderByTimestampDesc(hostId).stream()
                .map(this::toResponse)
                .toList();
    }

    private Host findHost(Long id) {
        return hostRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Host with id " + id + " not found"));
    }

    private HostStatus simulateStatus(String ipAddress) {
        int lastOctet = extractLastOctet(ipAddress);
        int mod = lastOctet % 4;
        if (mod == 0) {
            return HostStatus.UP;
        }
        if (mod == 1) {
            return HostStatus.DEGRADED;
        }
        if (mod == 2) {
            return HostStatus.DOWN;
        }
        return HostStatus.UP;
    }

    private long calculateSimulatedLatency(String ipAddress) {
        int lastOctet = extractLastOctet(ipAddress);
        return 20L + (lastOctet % 50) * 5L;
    }

    private int extractLastOctet(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        if (parts.length == 0) {
            return 0;
        }
        try {
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private HostCheckResultResponse toResponse(HostCheckResult result) {
        HostCheckResultResponse response = new HostCheckResultResponse();
        response.setId(result.getId());
        response.setHostId(result.getHost().getId());
        response.setTimestamp(result.getTimestamp());
        response.setStatus(result.getStatus().name());
        response.setResponseTimeMillis(result.getResponseTimeMillis());
        response.setErrorMessage(result.getErrorMessage());
        return response;
    }
}
