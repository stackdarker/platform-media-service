package com.stackdarker.platform.media.persistence;

import com.stackdarker.platform.media.domain.MediaObjectStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "media_objects")
public class MediaObjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 100)
    private String bucket = "media";

    @Column(name = "object_key", nullable = false, length = 500)
    private String objectKey;

    @Column(name = "original_filename", length = 255)
    private String originalFilename;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "content_length", nullable = false)
    private Long contentLength;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MediaObjectStatus status = MediaObjectStatus.PENDING;

    @Column(length = 64)
    private String checksum;

    @Column(name = "upload_url_expires_at")
    private Instant uploadUrlExpiresAt;

    @Column(name = "uploaded_at")
    private Instant uploadedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }

    public String getObjectKey() { return objectKey; }
    public void setObjectKey(String objectKey) { this.objectKey = objectKey; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Long getContentLength() { return contentLength; }
    public void setContentLength(Long contentLength) { this.contentLength = contentLength; }

    public MediaObjectStatus getStatus() { return status; }
    public void setStatus(MediaObjectStatus status) { this.status = status; }

    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }

    public Instant getUploadUrlExpiresAt() { return uploadUrlExpiresAt; }
    public void setUploadUrlExpiresAt(Instant uploadUrlExpiresAt) { this.uploadUrlExpiresAt = uploadUrlExpiresAt; }

    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
