package com.stackdarker.platform.media.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(
            @Value("${spring.application.name:platform-media-service}") String appName
    ) {
        return registry -> registry.config().commonTags(
                "service", appName
        );
    }
}
