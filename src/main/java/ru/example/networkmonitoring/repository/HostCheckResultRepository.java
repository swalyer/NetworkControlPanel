package ru.example.networkmonitoring.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.networkmonitoring.model.HostCheckResult;

public interface HostCheckResultRepository extends JpaRepository<HostCheckResult, Long> {
    List<HostCheckResult> findTop10ByHostIdOrderByTimestampDesc(Long hostId);
    List<HostCheckResult> findByHostIdOrderByTimestampDesc(Long hostId);
}
