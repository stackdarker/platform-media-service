package com.stackdarker.platform.media.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket = "media";
    private int uploadUrlExpiryMinutes = 60;
    private int downloadUrlExpiryMinutes = 15;

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }

    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }

    public int getUploadUrlExpiryMinutes() { return uploadUrlExpiryMinutes; }
    public void setUploadUrlExpiryMinutes(int uploadUrlExpiryMinutes) { this.uploadUrlExpiryMinutes = uploadUrlExpiryMinutes; }

    public int getDownloadUrlExpiryMinutes() { return downloadUrlExpiryMinutes; }
    public void setDownloadUrlExpiryMinutes(int downloadUrlExpiryMinutes) { this.downloadUrlExpiryMinutes = downloadUrlExpiryMinutes; }
}
