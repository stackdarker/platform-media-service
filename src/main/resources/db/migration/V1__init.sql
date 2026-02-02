-- Media service initial schema
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE media_objects (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    bucket VARCHAR(100) NOT NULL DEFAULT 'media',
    object_key VARCHAR(500) NOT NULL,
    original_filename VARCHAR(255),
    content_type VARCHAR(100) NOT NULL,
    content_length BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    checksum VARCHAR(64),
    upload_url_expires_at TIMESTAMPTZ,
    uploaded_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ,

    UNIQUE(bucket, object_key)
);

CREATE INDEX idx_media_objects_user_id ON media_objects(user_id);
CREATE INDEX idx_media_objects_status ON media_objects(status);
CREATE INDEX idx_media_objects_created_at ON media_objects(created_at);
