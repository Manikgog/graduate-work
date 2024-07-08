package ru.skypro.homework.service;

import org.springframework.stereotype.Component;
import ru.skypro.homework.exceptions.WrongLengthException;
import ru.skypro.homework.exceptions.WrongNumberException;

@Component
public class CheckService {
    public void checkString(int minLength, int maxLength, String string) {
        if(string.length() < minLength){
            throw new WrongLengthException("Слишком короткий текст: " + string);
        }else if(string.length() > maxLength){
            throw new WrongLengthException("Слишком длинный текст: " + string);
        }
    }

    public void checkNumber(int minLength, int maxLength, int number) {
        if(number < minLength){
            throw new WrongNumberException("Слишком маленькая цифра: " + number);
        }else if(number > maxLength){
            throw new WrongNumberException("Слишком большая цифра: " + number);
        }
    }
}
