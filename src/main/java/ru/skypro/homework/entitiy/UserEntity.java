package ru.skypro.homework.entitiy;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.skypro.homework.dto.Role;

@Entity
@Table(name = "user_table")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "email", columnDefinition = "VARCHAR(32)")
    String email;

    @Column(name = "first_name", columnDefinition = "VARCHAR(16)")
    String firstName;

    @Column(name = "last_name", columnDefinition = "VARCHAR(16)")
    String lastName;

    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "VARCHAR(5)")
    Role role;

    @Column(name = "image", nullable = false, columnDefinition = "TEXT")
    String image;

    @Column(name = "password", nullable = false)
    String password;
}
