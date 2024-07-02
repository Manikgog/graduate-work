package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Register {
    @Schema(minLength = 4, maxLength = 32, description = "логин")
    String username;
    @Schema(minLength = 8, maxLength = 16, description = "пароль")
    String password;
    @Schema(minLength = 2, maxLength = 16, description = "имя пользователя")
    String firstName;
    @Schema(minLength = 2, maxLength = 16, description = "фамилия пользователя")
    String lastName;
    @Schema(pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", description = "телефон пользователя")
    String phone;
    @Schema(description = "роль пользователя")
    Role role;
}
