package ru.skypro.homework.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class UserRegister {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRegister that = (UserRegister) o;
        return Objects.equals(userName, that.userName) && Objects.equals(password, that.password) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(phone, that.phone) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, firstName, lastName, phone, role);
    }
}
