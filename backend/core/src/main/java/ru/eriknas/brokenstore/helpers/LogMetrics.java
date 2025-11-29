package ru.eriknas.brokenstore.helpers;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class LogMetrics {
    private final Counter infoCounter;
    private final Counter warnCounter;
    private final Counter errorCounter;

    public LogMetrics(MeterRegistry registry) {
        this.infoCounter = Counter.builder("log_level_info")
                .description("Number of INFO logs")
                .tag("level", "INFO")
                .register(registry);
        this.warnCounter = Counter.builder("log_level_warn")
                .description("Number of WARN logs")
                .tag("level", "WARN")
                .register(registry);
        this.errorCounter = Counter.builder("log_level_error")
                .description("Number of ERROR logs")
                .tag("level", "ERROR")
                .register(registry);
    }

    public void incrementInfo() {
        infoCounter.increment();
    }

    public void incrementWarn() {
        warnCounter.increment();
    }

    public void incrementError() {
        errorCounter.increment();
    }
}
