package ru.skypro.homework.entity;

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

    public UserEntity() {}

    public UserEntity(UserEntity userEntity) {
        this.id = userEntity.id;
        this.email = userEntity.email;
        this.firstName = userEntity.firstName;
        this.lastName = userEntity.lastName;
        this.password = userEntity.password;
        this.phone = userEntity.phone;
        this.image = userEntity.image;
        this.role = userEntity.role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(32)", unique = true)
    String email;

    @Column(name = "first_name", nullable = false, columnDefinition = "VARCHAR(16)")
    String firstName;

    @Column(name = "last_name", nullable = false, columnDefinition = "VARCHAR(16)")
    String lastName;

    @Column(name = "phone", nullable = false, columnDefinition = "VARCHAR(20)")
    String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "VARCHAR(5)")
    Role role;

    @Column(name = "image", columnDefinition = "TEXT")
    String image;

    @Column(name = "password", nullable = false)
    String password;

}
