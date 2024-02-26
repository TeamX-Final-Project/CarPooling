package org.example.carpooling.exceptions;

public class EntityDuplicateException extends RuntimeException{

    public EntityDuplicateException(String type, String atribute, String value) {
        super(String.format("%s with %s %s already exists.", type, atribute, value));
    }
}
