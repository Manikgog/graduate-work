package ru.skypro.homework.exceptions;

public class WrongNumberException extends RuntimeException {
    private final String message;

    public WrongNumberException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
