package ru.example.networkmonitoring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.example.networkmonitoring.model.Host;
import ru.example.networkmonitoring.model.HostCheckResult;
import ru.example.networkmonitoring.model.HostStatus;
import ru.example.networkmonitoring.service.MonitoringService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {
        "monitoring.simulation-mode=true",
        "monitoring.timeout-millis=500"
})
class MonitoringServiceTests {

    @Autowired
    private MonitoringService monitoringService;

    @Test
    void checkHostAvailabilityUpdatesLastStatus() {
        Host host = new Host();
        host.setName("Gateway");
        host.setIpAddress("192.168.0.10");
        host.setRole("gateway");
        Host saved = monitoringService.createHost(host);

        HostCheckResult result = monitoringService.checkHostAvailability(saved.getId());

        assertThat(result.getHostId()).isEqualTo(saved.getId());
        assertThat(result.getStatus()).isIn(HostStatus.UP, HostStatus.DOWN, HostStatus.DEGRADED);
        assertThat(monitoringService.getHost(saved.getId()).getLastStatus()).isEqualTo(result.getStatus());
        assertThat(monitoringService.getHost(saved.getId()).getLastCheckTime()).isNotNull();
    }

    @Test
    void deleteHostRemovesEntry() {
        Host host = new Host();
        host.setName("File server");
        host.setIpAddress("192.168.0.20");
        host.setRole("fileserver");
        Host saved = monitoringService.createHost(host);

        monitoringService.deleteHost(saved.getId());

        assertThatThrownBy(() -> monitoringService.getHost(saved.getId()))
                .isInstanceOf(RuntimeException.class);
    }
}
