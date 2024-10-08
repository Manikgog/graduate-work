package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Login {
    @Schema(minLength = 8, maxLength = 16, description = "пароль")
    String password;
    @Schema(minLength = 4, maxLength = 32, description = "логин")
    String username;
}
