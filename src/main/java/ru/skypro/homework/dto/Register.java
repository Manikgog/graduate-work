package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Register {
    private String userName;
    @Schema(example = "stringst")
    private String password;
    private String firstName;
    private String lastName;
    @Schema(example = "+7 (223229-1130")
    private String phone;
    private Role role;
}
