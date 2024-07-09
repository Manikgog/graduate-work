package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;

public interface AdRepo extends JpaRepository<AdEntity, Long> {
    List<AdEntity> findByAuthor(UserEntity authorEntity);
}
