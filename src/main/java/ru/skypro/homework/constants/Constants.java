package ru.skypro.homework.constants;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PUBLIC)
public class Constants {
    final int MIN_LENGTH_USERNAME = 4;
    final int MAX_LENGTH_USERNAME = 32;
    final int MIN_LENGTH_TITLE_AD = 4;
    final int MAX_LENGTH_TITLE_AD = 32;
    final int MIN_LENGTH_DESCRIPTION_AD = 8;
    final int MAX_LENGTH_DESCRIPTION_AD = 64;
    final int MIN_PRICE = 0;
    final int MAX_PRICE = 10_000_000;
    final int MIN_LENGTH_PASSWORD = 8;
    final int MAX_LENGTH_PASSWORD = 16;
    final int MIN_LENGTH_FIRSTNAME = 2;
    final int MAX_LENGTH_FIRSTNAME = 16;
    final int MIN_LENGTH_LASTNAME = 2;
    final int MAX_LENGTH_LASTNAME = 16;
    final String PHONE_PATTERN = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}";
}
