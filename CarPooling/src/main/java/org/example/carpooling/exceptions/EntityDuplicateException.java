package org.example.carpooling.exceptions;

public class EntityDuplicateException extends RuntimeException {
    private String attribute;

    public EntityDuplicateException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists.", type, attribute, value));
        this.attribute = attribute;
    }

    public EntityDuplicateException(String value) {
        super(String.format(value));
    }

    public String getAttribute() {
        return attribute;
    }
}
