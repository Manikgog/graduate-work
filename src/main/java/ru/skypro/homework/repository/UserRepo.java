package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entitiy.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {
}
