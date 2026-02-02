package com.stackdarker.platform.media.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateUploadRequest(
        @NotBlank(message = "File name is required")
        @Size(max = 255, message = "File name must be at most 255 characters")
        String fileName,

        @NotBlank(message = "Content type is required")
        @Size(max = 100, message = "Content type must be at most 100 characters")
        String contentType,

        @Positive(message = "Content length must be positive")
        Long contentLength
) {}
