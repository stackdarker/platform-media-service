package com.stackdarker.platform.media.api;

import com.stackdarker.platform.media.api.dto.*;
import com.stackdarker.platform.media.service.MediaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/uploads")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUploadResponse createUploadIntent(
            @Valid @RequestBody CreateUploadRequest request,
            Authentication authentication
    ) {
        UUID userId = extractUserId(authentication);
        return mediaService.createUploadIntent(userId, request);
    }

    @GetMapping("/objects/{objectId}")
    public MediaObjectResponse getObject(
            @PathVariable UUID objectId,
            Authentication authentication
    ) {
        UUID userId = extractUserId(authentication);
        return mediaService.getObjectMetadata(userId, objectId);
    }

    @GetMapping("/objects/{objectId}/download")
    public DownloadUrlResponse getDownloadUrl(
            @PathVariable UUID objectId,
            Authentication authentication
    ) {
        UUID userId = extractUserId(authentication);
        return mediaService.generateDownloadUrl(userId, objectId);
    }

    private UUID extractUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
