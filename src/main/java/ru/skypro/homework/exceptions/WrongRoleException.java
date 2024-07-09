package ru.skypro.homework.exceptions;

public class WrongRoleException extends RuntimeException {
    private final String message;

    public WrongRoleException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
