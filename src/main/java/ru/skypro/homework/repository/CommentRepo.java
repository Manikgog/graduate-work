package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.CommentEntity;

public interface CommentRepo extends JpaRepository<CommentEntity, Long> {
}
