package ru.skypro.homework.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class UserPatch {
    private String firstName;
    private String lastName;
    private String phone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPatch userPatch = (UserPatch) o;
        return Objects.equals(firstName, userPatch.firstName) && Objects.equals(lastName, userPatch.lastName) && Objects.equals(phone, userPatch.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phone);
    }
}
