package com.stackdarker.platform.media.api.dto;

import java.time.Instant;
import java.util.UUID;

public record MediaObjectResponse(
        UUID id,
        String fileName,
        String contentType,
        Long sizeBytes,
        Instant createdAt
) {}
