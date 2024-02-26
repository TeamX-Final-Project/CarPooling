package org.example.carpooling.exceptions;

public class PasswordChangeProfileError extends RuntimeException{
    public PasswordChangeProfileError(String message) {
        super(message);
    }
}
