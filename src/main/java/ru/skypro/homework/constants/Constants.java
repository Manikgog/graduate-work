package ru.skypro.homework.constants;

import org.springframework.stereotype.Component;

@Component
public class Constants {
    public static final int MIN_PRICE = 0;
    public static final int MAX_PRICE = 10_000_000;
    public static final String PHONE_PATTERN = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}";
}
