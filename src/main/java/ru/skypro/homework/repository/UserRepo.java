package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entitiy.UserEntity;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByFirstNameAndLastName(String firstName, String lastName);
}
