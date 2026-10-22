package com.hoane.fbhelper.fbhelperextentionbe.exception;

public class ConflictException extends RuntimeException {
    private String name;

    public ConflictException(String message, String name) {
        super(message);
        this.name = name;
    }

    public ConflictException(String message) {
        super(message);

    }

    public String getName() {
        return this.name;
    }
}
