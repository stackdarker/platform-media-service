package com.stackdarker.platform.media.api;

import com.stackdarker.platform.media.api.dto.*;
import com.stackdarker.platform.media.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/media")
@Tag(name = "Media", description = "File uploads, metadata, and download URLs")
@SecurityRequirement(name = "bearer-jwt")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @Operation(summary = "Create upload intent", description = "Returns a pre-signed URL for uploading a file to MinIO")
    @ApiResponse(responseCode = "201", description = "Upload intent created")
    @ApiResponse(responseCode = "422", description = "Validation failed")
    @PostMapping("/uploads")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUploadResponse createUploadIntent(
            @Valid @RequestBody CreateUploadRequest request,
            Authentication authentication
    ) {
        UUID userId = extractUserId(authentication);
        return mediaService.createUploadIntent(userId, request);
    }

    @Operation(summary = "Get object metadata", description = "Returns metadata for an uploaded media object")
    @ApiResponse(responseCode = "200", description = "Metadata retrieved")
    @ApiResponse(responseCode = "404", description = "Object not found")
    @GetMapping("/objects/{objectId}")
    public MediaObjectResponse getObject(
            @PathVariable UUID objectId,
            Authentication authentication
    ) {
        UUID userId = extractUserId(authentication);
        return mediaService.getObjectMetadata(userId, objectId);
    }

    @Operation(summary = "Get download URL", description = "Returns a time-limited pre-signed download URL")
    @ApiResponse(responseCode = "200", description = "Download URL generated")
    @ApiResponse(responseCode = "404", description = "Object not found")
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
