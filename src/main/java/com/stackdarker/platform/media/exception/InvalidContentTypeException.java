package com.stackdarker.platform.media.exception;

public class InvalidContentTypeException extends RuntimeException {

    public InvalidContentTypeException(String contentType) {
        super("Content type not allowed: " + contentType);
    }
}
