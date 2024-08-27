package ru.skypro.homework.exceptions;

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