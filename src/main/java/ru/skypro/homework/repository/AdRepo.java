package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface AdRepo extends JpaRepository<AdEntity, Long> {
    List<AdEntity> findByAuthor(UserEntity authorEntity);
    Optional<AdEntity> findByTitle(String title);
}
