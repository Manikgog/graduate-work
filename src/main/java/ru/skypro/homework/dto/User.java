package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Сущность пользователя")
public class User {
    @Schema(description = "Идентификатор", example = "1")
    long id;
    @Schema(description = "Email", example = "example@mail.ru")
    String email;
    @Schema(description = "Имя")
    String firstName;
    @Schema(description = "Фамилия")
    String lastName;
    @Schema(description = "Телефон", example = "+7 768 4177409")
    String phone;
    @Schema(description = "Роль", example = "USER")
    Role role;
    @Schema(description = "Адрес файла с фотографией", example = "/image.jpg")
    String image;
}
