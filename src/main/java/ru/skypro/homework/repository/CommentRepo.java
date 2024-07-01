package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entitiy.CommentEntity;

public interface CommentRepo extends JpaRepository<CommentEntity, Long> {
}
