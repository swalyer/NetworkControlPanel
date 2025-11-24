package ru.example.networkmonitoring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "monitoring")
public class MonitoringProperties {

    private boolean simulationMode;
    private int timeoutMillis;
    private Scheduling scheduling = new Scheduling();

    public boolean isSimulationMode() {
        return simulationMode;
    }

    public void setSimulationMode(boolean simulationMode) {
        this.simulationMode = simulationMode;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public Scheduling getScheduling() {
        return scheduling;
    }

    public void setScheduling(Scheduling scheduling) {
        this.scheduling = scheduling;
    }

    public static class Scheduling {
        private boolean enabled;
        private long fixedDelayMillis;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public long getFixedDelayMillis() {
            return fixedDelayMillis;
        }

        public void setFixedDelayMillis(long fixedDelayMillis) {
            this.fixedDelayMillis = fixedDelayMillis;
        }
    }
}
