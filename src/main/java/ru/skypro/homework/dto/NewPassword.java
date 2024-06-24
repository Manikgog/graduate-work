package ru.skypro.homework.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
public class NewPassword {
    private String currentPassword;
    private String newPassword;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewPassword that = (NewPassword) o;
        return Objects.equals(currentPassword, that.currentPassword) && Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPassword, newPassword);
    }
}
