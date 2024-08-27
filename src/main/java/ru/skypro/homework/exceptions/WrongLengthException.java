package ru.skypro.homework.exceptions;

public class WrongLengthException extends RuntimeException{
    private final String message;

    public WrongLengthException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
