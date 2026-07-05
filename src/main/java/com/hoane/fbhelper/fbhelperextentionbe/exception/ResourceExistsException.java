package com.hoane.fbhelper.fbhelperextentionbe.exception;

public class ResourceExistsException extends RuntimeException {
    private String name;

    public ResourceExistsException(String name, String message) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
