package com.littlefoxstudios.identityreconciliation.exceptionhandler;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
