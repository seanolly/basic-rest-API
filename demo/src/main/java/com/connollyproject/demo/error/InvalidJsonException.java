package com.connollyproject.demo.error;

/**
 * Thrown when invalid json is parsed.
 */
public class InvalidJsonException extends Exception {
    public InvalidJsonException() {
        super("Invalid JSON Object");
    }
}
