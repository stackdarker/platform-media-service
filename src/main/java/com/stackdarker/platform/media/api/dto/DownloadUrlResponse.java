package com.stackdarker.platform.media.api.dto;

import java.time.Instant;

public record DownloadUrlResponse(
        String url,
        Instant expiresAt
) {}
