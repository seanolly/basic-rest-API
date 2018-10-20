package com.connollyproject.demo.error;

/**
 * Thrown when an invalid UUID is parsed.
 */
public class InvalidUUIDException extends Exception {
    public InvalidUUIDException() {
        super("Invalid UUID");
    }
}
