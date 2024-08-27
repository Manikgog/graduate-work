package ru.skypro.homework.check;

import org.springframework.stereotype.Component;
import ru.skypro.homework.exceptions.WrongNumberException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static ru.skypro.homework.constants.Constants.PHONE_PATTERN;

@Component
public class CheckService {
    private final Pattern pat = Pattern.compile(PHONE_PATTERN);

    public void checkNumber(int minLength, int maxLength, int number) {
        if(number < minLength){
            throw new WrongNumberException("Слишком маленькая цифра: " + number);
        }else if(number > maxLength){
            throw new WrongNumberException("Слишком большая цифра: " + number);
        }
    }

    public void checkPhone(String phone){
        Matcher mat = pat.matcher(phone);
        if(!mat.matches()){
            throw new WrongNumberException("Номер телефона не соответствует образцу");
        }
    }
}
