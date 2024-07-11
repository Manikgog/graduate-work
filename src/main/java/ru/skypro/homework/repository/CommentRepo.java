package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepo extends JpaRepository<CommentEntity, Long> {
    Optional<List<CommentEntity>> findByAdId(Long adId);
}
