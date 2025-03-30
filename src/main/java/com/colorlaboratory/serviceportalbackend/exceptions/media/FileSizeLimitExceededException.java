package com.colorlaboratory.serviceportalbackend.exceptions.media;

public class FileSizeLimitExceededException extends RuntimeException {
    public FileSizeLimitExceededException(String errorMessage) {
        super(errorMessage);
    }
}
