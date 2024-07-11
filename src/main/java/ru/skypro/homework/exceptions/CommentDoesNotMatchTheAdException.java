package ru.skypro.homework.exceptions;

public class CommentDoesNotMatchTheAdException extends RuntimeException {
    private final String message;

    public CommentDoesNotMatchTheAdException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage(){
        return message;
    }
}
