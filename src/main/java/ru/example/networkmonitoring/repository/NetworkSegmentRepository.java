package ru.example.networkmonitoring.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.networkmonitoring.model.NetworkSegment;

public interface NetworkSegmentRepository extends JpaRepository<NetworkSegment, Long> {
    Optional<NetworkSegment> findByName(String name);
}
