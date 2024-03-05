package org.example.carpooling.exceptions;

public class ValidatePasswordException extends RuntimeException {
    public ValidatePasswordException(String message) {
        super(message);
    }
}
