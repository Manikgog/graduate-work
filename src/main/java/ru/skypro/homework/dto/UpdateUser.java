package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUser {
    @Schema(minLength = 3, maxLength = 10, description = "имя пользователя")
    String firstName;
    @Schema(minLength = 3, maxLength = 10, description = "фамилия пользователя")
    String lastName;
    @Schema(pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", description = "телефон пользователя")
    String phone;
}
