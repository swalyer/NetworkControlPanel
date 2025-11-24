package ru.example.networkmonitoring.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.networkmonitoring.model.Host;

public interface HostRepository extends JpaRepository<Host, Long> {
    List<Host> findByNetworkSegmentId(Long segmentId);
    List<Host> findByMonitoredTrue();
    Optional<Host> findByIpAddress(String ipAddress);
}
