package ru.skypro.homework.service;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exceptions.WrongLengthException;
import ru.skypro.homework.exceptions.WrongNumberException;
import ru.skypro.homework.exceptions.WrongPasswordException;
import ru.skypro.homework.exceptions.WrongRoleException;
import ru.skypro.homework.repository.UserRepo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void checkPhone(String pattern, String phone){
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(phone);
        if(!mat.matches()){
            throw new WrongNumberException("Номер телефона не соответствует образцу");
        }
    }

    public void checkRole(Role role){
        for (var item : Role.values()){
            if(item.equals(role)){
                return;
            }
        }
        throw new WrongRoleException("Указана несуществующая роль");
    }

}
