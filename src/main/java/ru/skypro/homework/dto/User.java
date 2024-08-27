package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Сущность пользователя")
public class User {
    @Schema(description = "id пользователя")
    long id;
    @Schema(description = "логин пользователя")
    String email;
    @Schema(description = "имя пользователя")
    String firstName;
    @Schema(description = "фамилия пользователя")
    String lastName;
    @Schema(description = "телефон пользователя", example = "+7 768 4177409")
    String phone;
    @Schema(description = "роль пользователя")
    Role role;
    @Schema(description = "ссылка на аватар пользователя")
    String image;
}
