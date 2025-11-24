package ru.example.networkmonitoring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.example.networkmonitoring.exception.NotFoundException;
import ru.example.networkmonitoring.model.Host;
import ru.example.networkmonitoring.model.HostCheckResult;
import ru.example.networkmonitoring.model.HostStatus;
import ru.example.networkmonitoring.repository.InMemoryHostRepository;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MonitoringService {

    private final InMemoryHostRepository hostRepository;
    private final boolean simulationMode;
    private final int timeoutMillis;
    private final AtomicLong checkResultId = new AtomicLong(0);

    public MonitoringService(InMemoryHostRepository hostRepository,
                             @Value("${monitoring.simulation-mode:true}") boolean simulationMode,
                             @Value("${monitoring.timeout-millis:1000}") int timeoutMillis) {
        this.hostRepository = hostRepository;
        this.simulationMode = simulationMode;
        this.timeoutMillis = timeoutMillis;
    }

    public List<Host> listHosts() {
        return hostRepository.findAll();
    }

    public Host getHost(Long id) {
        return hostRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Host with id " + id + " not found"));
    }

    public Host createHost(Host host) {
        host.setLastStatus(HostStatus.UNKNOWN);
        host.setLastCheckTime(null);
        return hostRepository.save(host);
    }

    public void deleteHost(Long id) {
        if (hostRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Host with id " + id + " not found");
        }
        hostRepository.deleteById(id);
    }

    public HostCheckResult checkHostAvailability(Long hostId) {
        Host host = getHost(hostId);

        HostStatus status;
        Long responseTime = null;
        String message;
        long start = System.nanoTime();

        if (simulationMode) {
            status = simulateStatus(host.getIpAddress());
            if (status != HostStatus.DOWN) {
                responseTime = Math.abs(extractLastOctet(host.getIpAddress()) * 10L) + 20L;
            }
            message = "Simulated status for host";
        } else {
            try {
                InetAddress address = InetAddress.getByName(host.getIpAddress());
                boolean reachable = address.isReachable(timeoutMillis);
                long elapsed = Duration.ofNanos(System.nanoTime() - start).toMillis();
                status = reachable ? HostStatus.UP : HostStatus.DOWN;
                responseTime = reachable ? elapsed : null;
                message = reachable ? "Host responded" : "No response within timeout";
            } catch (IOException e) {
                status = HostStatus.DOWN;
                message = "Error checking host: " + e.getMessage();
            }
        }

        OffsetDateTime timestamp = OffsetDateTime.now();
        host.setLastStatus(status);
        host.setLastCheckTime(timestamp);
        hostRepository.save(host);

        long id = checkResultId.incrementAndGet();
        return new HostCheckResult(id, hostId, timestamp, status, responseTime, message);
    }

    private HostStatus simulateStatus(String ipAddress) {
        int lastOctet = extractLastOctet(ipAddress);
        int mod = lastOctet % 3;
        return switch (mod) {
            case 0 -> HostStatus.UP;
            case 1 -> HostStatus.DEGRADED;
            default -> HostStatus.DOWN;
        };
    }

    private int extractLastOctet(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        try {
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
