package ru.example.networkmonitoring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.example.networkmonitoring.dto.HostCheckResultResponse;
import ru.example.networkmonitoring.exception.NotFoundException;
import ru.example.networkmonitoring.model.Host;
import ru.example.networkmonitoring.repository.HostRepository;
import ru.example.networkmonitoring.service.MonitoringService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MonitoringServiceTests {

    @Autowired
    private MonitoringService monitoringService;

    @Autowired
    private HostRepository hostRepository;

    @Test
    void checkHostAvailabilityReturnsResult() {
        Host host = hostRepository.findByIpAddress("192.168.10.1").orElseThrow();
        HostCheckResultResponse result = monitoringService.checkHostAvailability(host.getId());
        assertThat(result.getHostId()).isEqualTo(host.getId());
        assertThat(result.getStatus()).isNotBlank();
    }

    @Test
    void notFoundHostThrowsException() {
        assertThatThrownBy(() -> monitoringService.checkHostAvailability(99999L))
                .isInstanceOf(NotFoundException.class);
    }
}
