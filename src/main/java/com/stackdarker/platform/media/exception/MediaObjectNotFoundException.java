package com.stackdarker.platform.media.exception;

import java.util.UUID;

public class MediaObjectNotFoundException extends RuntimeException {

    public MediaObjectNotFoundException(UUID objectId) {
        super("Media object not found: " + objectId);
    }
}
