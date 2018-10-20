package com.connollyproject.demo.error;

import org.springframework.lang.NonNull;

import java.util.UUID;

/**
 * Thrown when Savable not found.
 */
public class SavableNotFoundException extends Exception {
    public SavableNotFoundException(@NonNull UUID uid) {
        super(String.format("Item with uid %s not found", uid));
    }
}
