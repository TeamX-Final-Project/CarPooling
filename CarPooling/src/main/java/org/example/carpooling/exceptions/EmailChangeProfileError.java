package org.example.carpooling.exceptions;

public class EmailChangeProfileError extends RuntimeException{
    public EmailChangeProfileError(String message) {
        super(message);
    }
}