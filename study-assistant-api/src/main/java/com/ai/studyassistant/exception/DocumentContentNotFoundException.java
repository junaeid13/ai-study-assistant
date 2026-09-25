package com.ai.studyassistant.exception;

public class DocumentContentNotFoundException extends RuntimeException {
    public DocumentContentNotFoundException(String message) {
        super(message);
    }
}
