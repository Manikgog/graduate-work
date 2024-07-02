package ru.skypro.homework.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ImageNotFoundException extends RuntimeException {
    private final String message;

    public ImageNotFoundException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}