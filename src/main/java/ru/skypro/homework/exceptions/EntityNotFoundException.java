package ru.skypro.homework.exceptions;


public class EntityNotFoundException extends RuntimeException {
    private final String message;

    public EntityNotFoundException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
