package com.colorlaboratory.serviceportalbackend.exceptions.media;

public class GcsMediaFileNotFoundException extends RuntimeException {
    public GcsMediaFileNotFoundException(String message) {
        super(message);
    }
}
