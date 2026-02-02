package com.stackdarker.platform.media.api.dto;

import java.time.Instant;
import java.util.UUID;

public record CreateUploadResponse(
        UUID objectId,
        String uploadUrl,
        Instant expiresAt
) {}
