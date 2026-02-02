package com.stackdarker.platform.media.service;

import com.stackdarker.platform.media.api.dto.*;
import com.stackdarker.platform.media.config.MediaProperties;
import com.stackdarker.platform.media.config.MinioProperties;
import com.stackdarker.platform.media.domain.MediaObjectStatus;
import com.stackdarker.platform.media.exception.FileSizeLimitExceededException;
import com.stackdarker.platform.media.exception.InvalidContentTypeException;
import com.stackdarker.platform.media.exception.MediaObjectNotFoundException;
import com.stackdarker.platform.media.metrics.MediaMetrics;
import com.stackdarker.platform.media.persistence.MediaObjectEntity;
import com.stackdarker.platform.media.persistence.MediaObjectRepository;
import com.stackdarker.platform.media.service.storage.MinioStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class MediaService {

    private static final Logger log = LoggerFactory.getLogger(MediaService.class);

    private final MediaObjectRepository mediaObjectRepository;
    private final MinioStorageService storageService;
    private final MediaProperties mediaProperties;
    private final MinioProperties minioProperties;
    private final MediaMetrics metrics;

    public MediaService(
            MediaObjectRepository mediaObjectRepository,
            MinioStorageService storageService,
            MediaProperties mediaProperties,
            MinioProperties minioProperties,
            MediaMetrics metrics
    ) {
        this.mediaObjectRepository = mediaObjectRepository;
        this.storageService = storageService;
        this.mediaProperties = mediaProperties;
        this.minioProperties = minioProperties;
        this.metrics = metrics;
    }

    @Transactional
    public CreateUploadResponse createUploadIntent(UUID userId, CreateUploadRequest request) {
        log.info("Creating upload intent for user {} - file: {}, type: {}, size: {}",
                userId, request.fileName(), request.contentType(), request.contentLength());

        // Validate content type
        if (!mediaProperties.getAllowedContentTypes().contains(request.contentType())) {
            throw new InvalidContentTypeException(request.contentType());
        }

        // Validate file size
        if (request.contentLength() != null && request.contentLength() > mediaProperties.getMaxFileSizeBytes()) {
            throw new FileSizeLimitExceededException(request.contentLength(), mediaProperties.getMaxFileSizeBytes());
        }

        // Generate unique object key
        String objectKey = generateObjectKey(userId, request.fileName());

        // Calculate expiry time
        Instant expiresAt = Instant.now().plus(minioProperties.getUploadUrlExpiryMinutes(), ChronoUnit.MINUTES);

        // Create media object record
        MediaObjectEntity mediaObject = new MediaObjectEntity();
        mediaObject.setUserId(userId);
        mediaObject.setBucket(storageService.getBucket());
        mediaObject.setObjectKey(objectKey);
        mediaObject.setOriginalFilename(request.fileName());
        mediaObject.setContentType(request.contentType());
        mediaObject.setContentLength(request.contentLength() != null ? request.contentLength() : 0L);
        mediaObject.setStatus(MediaObjectStatus.PENDING);
        mediaObject.setUploadUrlExpiresAt(expiresAt);

        mediaObject = mediaObjectRepository.save(mediaObject);

        // Generate pre-signed upload URL
        String uploadUrl = storageService.generatePresignedPutUrl(objectKey, request.contentType());

        metrics.recordUploadIntentCreated();

        return new CreateUploadResponse(
                mediaObject.getId(),
                uploadUrl,
                expiresAt
        );
    }

    @Transactional(readOnly = true)
    public MediaObjectResponse getObjectMetadata(UUID userId, UUID objectId) {
        MediaObjectEntity mediaObject = mediaObjectRepository.findByIdAndUserId(objectId, userId)
                .orElseThrow(() -> new MediaObjectNotFoundException(objectId));

        return new MediaObjectResponse(
                mediaObject.getId(),
                mediaObject.getOriginalFilename(),
                mediaObject.getContentType(),
                mediaObject.getContentLength(),
                mediaObject.getCreatedAt()
        );
    }

    @Transactional
    public DownloadUrlResponse generateDownloadUrl(UUID userId, UUID objectId) {
        MediaObjectEntity mediaObject = mediaObjectRepository.findByIdAndUserId(objectId, userId)
                .orElseThrow(() -> new MediaObjectNotFoundException(objectId));

        // Check if object exists in storage and update status if needed
        if (mediaObject.getStatus() == MediaObjectStatus.PENDING) {
            if (storageService.objectExists(mediaObject.getObjectKey())) {
                mediaObject.setStatus(MediaObjectStatus.UPLOADED);
                mediaObject.setUploadedAt(Instant.now());
                mediaObjectRepository.save(mediaObject);
                metrics.recordUploadCompleted();
            }
        }

        String downloadUrl = storageService.generatePresignedGetUrl(mediaObject.getObjectKey());
        Instant expiresAt = Instant.now().plus(minioProperties.getDownloadUrlExpiryMinutes(), ChronoUnit.MINUTES);

        metrics.recordDownloadUrlGenerated();

        return new DownloadUrlResponse(downloadUrl, expiresAt);
    }

    private String generateObjectKey(UUID userId, String fileName) {
        String sanitizedFileName = sanitizeFileName(fileName);
        return String.format("%s/%s/%s",
                userId.toString(),
                UUID.randomUUID().toString(),
                sanitizedFileName
        );
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null) return "unnamed";
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
