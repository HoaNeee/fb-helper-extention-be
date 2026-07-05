package com.hoane.fbhelper.fbhelperextentionbe.exception;

public class ResourceNotFoundException extends RuntimeException {
    private String name;

    public ResourceNotFoundException(String name, String message) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
