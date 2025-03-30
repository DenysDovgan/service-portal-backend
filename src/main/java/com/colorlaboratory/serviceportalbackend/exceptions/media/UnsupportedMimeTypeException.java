package com.colorlaboratory.serviceportalbackend.exceptions.media;

public class UnsupportedMimeTypeException extends RuntimeException {

    public UnsupportedMimeTypeException(String errorMessage) {
        super(errorMessage);
    }
}
