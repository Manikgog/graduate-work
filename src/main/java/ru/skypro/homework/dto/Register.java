package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Register {
    @Schema(minLength = 4, maxLength = 32, description = "логин")
    private String userName;
    @Schema(minLength = 8, maxLength = 16, description = "пароль")
    private String password;
    @Schema(minLength = 2, maxLength = 16, description = "имя пользователя")
    private String firstName;
    @Schema(minLength = 2, maxLength = 16, description = "фамилия пользователя")
    private String lastName;
    @Schema(pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", description = "телефон пользователя")
    private String phone;
    @Schema(description = "роль пользователя")
    private Role role;
}
