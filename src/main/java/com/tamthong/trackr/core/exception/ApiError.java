package com.tamthong.trackr.core.exception;

import java.time.Instant;

public record ApiError(int status,
                       String error,
                       String message,
                       String path,
                       Instant timestamp) {
    public ApiError(int status, String error, String message, String path) {
        this(status, error, message, path, Instant.now());
    }
}
