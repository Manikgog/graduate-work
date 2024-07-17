package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.CommentEntity;
import java.util.List;

public interface CommentRepo extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByAdId(Long adId);
}
