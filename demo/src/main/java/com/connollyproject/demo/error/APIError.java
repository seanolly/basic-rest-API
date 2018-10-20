package com.connollyproject.demo.error;

import org.springframework.lang.NonNull;

/**
 * Represent API Errors.
 */
public class APIError {

    private final String verb;
    private final String url;
    private final String message;

    public APIError(@NonNull String verb,
                    @NonNull String url,
                    @NonNull String message) {
        this.verb = verb;
        this.url = url;
        this.message = message;
    }

    public String getVerb() {
        return verb;
    }

    public String getUrl() {
        return url;
    }

    public String getMessage() {
        return message;
    }
}
