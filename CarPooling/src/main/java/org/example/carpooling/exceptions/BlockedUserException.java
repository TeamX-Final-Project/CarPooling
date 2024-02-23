package org.example.carpooling.exceptions;

public class BlockedUserException extends RuntimeException{
    public BlockedUserException(String message) {
        super(message);
    }
}
