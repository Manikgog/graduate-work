package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entitiy.AdEntity;

public interface AdRepo extends JpaRepository<AdEntity, Long> {
}
