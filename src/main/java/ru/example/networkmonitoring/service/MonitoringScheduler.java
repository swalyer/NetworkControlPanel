package ru.example.networkmonitoring.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.example.networkmonitoring.config.MonitoringProperties;

@Component
public class MonitoringScheduler {

    private final MonitoringService monitoringService;
    private final MonitoringProperties monitoringProperties;

    public MonitoringScheduler(MonitoringService monitoringService, MonitoringProperties monitoringProperties) {
        this.monitoringService = monitoringService;
        this.monitoringProperties = monitoringProperties;
    }

    @Scheduled(fixedDelayString = "${monitoring.scheduling.fixed-delay-millis}")
    public void runScheduledChecks() {
        if (monitoringProperties.getScheduling().isEnabled()) {
            monitoringService.checkAllMonitoredHosts();
        }
    }
}
