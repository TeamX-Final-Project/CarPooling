package org.example.carpooling.exceptions;

public class EntityAttributeAlreadySetException extends RuntimeException{
    public EntityAttributeAlreadySetException(String type, String attribute, String value) {
        super(String.format("%s %s is already %s.", type, attribute, value));
    }
}

