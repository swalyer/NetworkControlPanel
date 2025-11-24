package ru.example.networkmonitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.example.networkmonitoring.config.MonitoringProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(MonitoringProperties.class)
public class NetworkMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetworkMonitoringApplication.class, args);
    }
}
