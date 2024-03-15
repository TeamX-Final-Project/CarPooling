package org.example.carpooling.exceptions;

public class TravelException extends RuntimeException{
    public TravelException (String message) {
        super(String.format("%s", message));
    }


}
