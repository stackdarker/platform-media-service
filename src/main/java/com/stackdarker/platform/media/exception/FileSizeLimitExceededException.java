package com.stackdarker.platform.media.exception;

public class FileSizeLimitExceededException extends RuntimeException {

    public FileSizeLimitExceededException(long size, long maxSize) {
        super("File size " + size + " bytes exceeds maximum allowed " + maxSize + " bytes");
    }
}
