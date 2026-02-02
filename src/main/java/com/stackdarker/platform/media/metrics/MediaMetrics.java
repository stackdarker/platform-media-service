package com.stackdarker.platform.media.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MediaMetrics {

    private final MeterRegistry registry;

    public MediaMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordUploadIntentCreated() {
        Counter.builder("media_upload_intent")
                .tag("result", "created")
                .description("Number of upload intents created")
                .register(registry)
                .increment();
    }

    public void recordUploadCompleted() {
        Counter.builder("media_upload")
                .tag("result", "success")
                .description("Number of uploads completed")
                .register(registry)
                .increment();
    }

    public void recordDownloadUrlGenerated() {
        Counter.builder("media_download_url")
                .tag("result", "generated")
                .description("Number of download URLs generated")
                .register(registry)
                .increment();
    }
}
