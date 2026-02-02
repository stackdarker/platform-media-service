# Platform Media Service

Media microservice for the platform ecosystem. Handles file upload/download with pre-signed URLs using MinIO (S3-compatible storage).

## Tech Stack

- Java 17
- Spring Boot 3.3.3
- PostgreSQL 16
- Redis 7 (rate limiting)
- MinIO (S3-compatible object storage)

## Endpoints

| Method | Path | Description | Auth |
|--------|------|-------------|------|
| GET | `/v1/health` | Health check | No |
| POST | `/v1/media/uploads` | Create upload intent | Yes |
| GET | `/v1/media/objects/{id}` | Get object metadata | Yes |
| GET | `/v1/media/objects/{id}/download` | Get download URL | Yes |

## Upload Flow

1. Client requests upload intent with file metadata
2. Service validates and returns pre-signed PUT URL
3. Client uploads file directly to MinIO using the URL
4. Client can then get metadata or download URL

## Local Development

### Prerequisites

- Java 17+
- Docker (for PostgreSQL, Redis, MinIO)
- Maven 3.9+

### Run locally

```bash
# Start dependencies
cd ../platform-infra
docker-compose up -d media-db redis minio

# Run the service
./mvnw spring-boot:run
```

### Run with full stack

```bash
cd ../platform-infra
docker-compose up -d --build
```

### Test endpoints

```bash
# Health check
curl http://localhost:8083/v1/health

# Create upload intent (requires JWT from auth-service)
curl -X POST http://localhost:8083/v1/media/uploads \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "fileName": "test.jpg",
    "contentType": "image/jpeg",
    "contentLength": 1024
  }'

# Upload file to returned uploadUrl
curl -X PUT "<uploadUrl>" \
  -H "Content-Type: image/jpeg" \
  --data-binary @test.jpg

# Get download URL
curl http://localhost:8083/v1/media/objects/<objectId>/download \
  -H "Authorization: Bearer <token>"

# MinIO Console
open http://localhost:9001
# Login: minioadmin / minioadmin123
```

## Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | 8083 | Service port |
| `minio.endpoint` | http://localhost:9000 | MinIO endpoint |
| `minio.access-key` | minioadmin | MinIO access key |
| `minio.secret-key` | minioadmin123 | MinIO secret key |
| `minio.bucket` | media | Default bucket |
| `media.max-file-size-bytes` | 104857600 | Max file size (100MB) |
| `app.jwt.secret` | - | JWT secret (must match auth-service) |

## Allowed Content Types

- image/jpeg, image/png, image/gif, image/webp
- video/mp4, video/webm
- application/pdf
- audio/mpeg, audio/wav

## Future Features

- [ ] Image processing (thumbnails, resize)
- [ ] Video transcoding pipeline
- [ ] CDN integration for downloads
- [ ] Virus scanning on upload
- [ ] Content moderation (AWS Rekognition)
- [ ] Folder/collection organization
- [ ] Sharing with other users
- [ ] Public/private visibility toggle
- [ ] Storage quotas per user
- [ ] Batch upload support
- [ ] Resumable uploads (tus protocol)
