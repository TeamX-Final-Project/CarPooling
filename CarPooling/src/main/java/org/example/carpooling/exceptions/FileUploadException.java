package org.example.carpooling.exceptions;

import java.io.IOException;

public class FileUploadException extends RuntimeException{
    public FileUploadException(String message, IOException e) {
        super(message);
    }
}
