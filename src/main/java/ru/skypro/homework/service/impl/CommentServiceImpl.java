package ru.skypro.homework.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepo;
import ru.skypro.homework.repository.CommentRepo;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;
    private final CommentMapper commentMapper;
    private final AdRepo adRepo;
    private final UserService userService;

    /**
     * Метод для получения комментариев объявления
     *
     * @param adId - идентификатор объявления
     * @return Comments - DTO комментариев объявлений
     */
    @Override
    public Comments get(Integer adId) {
        List<Comment> comment = commentRepo.findByAdId((long) adId).orElseThrow(() -> {
                    log.info("The get method of CommentServiceImpl is called");
                    return new EntityNotFoundException("Комментариев к объявлению с id=" + adId+" не найдено");
                }).stream()
                .map(commentMapper::CommentEntityToComment)
                .toList();
        Comments comments = new Comments();
        comments.setCount(comment.size());
        comments.setResults(comment);
        return comments;
    }

    /**
     * Метод для создания комментария к объявлению
     *
     * @param adId       - идентификатор объявления
     * @param newComment - объект с полями для создания нового комментария
     * @return Comment - DTO созданного комментария
     */
    @Override
    public Comment create(Integer adId, CreateOrUpdateComment newComment) {
        AdEntity adEntity = adRepo.findById((long) adId).orElseThrow(() -> {
            log.info("The create method of CommentServiceImpl is called");
            return new EntityNotFoundException("Объявление id=" + adId + " не найдено");
        });
        CommentEntity commentEntity = new CommentEntity();
        MyUserDetails myUserDetails = userService.getUserDetails();
        commentEntity.setAuthor(myUserDetails.getUser());
        commentEntity.setAd(adEntity);
        commentEntity.setCreatedAt(Instant.now().toEpochMilli());
        commentEntity.setText(newComment.getText());
        commentRepo.save(commentEntity);

        return commentMapper.CommentEntityToComment(commentEntity);
    }

    /**
     * Метод для удаления комментария к объявлению
     *
     * @param adId      - идентификатор объявления
     * @param commentId - идентификатор комментария
     */
    @Override
    public void delete(Integer adId, Integer commentId) {
        AdEntity adEntity = adRepo.findById((long) adId).orElseThrow(() -> {
            log.info("The delete method of CommentServiceImpl is called");
            return new EntityNotFoundException("Объявление id=" + adId + " не найдено");
        });
        commentRepo.deleteById((long) commentId);

    }

    /**
     * Метод для обновления комментария к объявлению
     *
     * @param adId       - идентификатор объявления
     * @param commentId  - идентификатор комментария
     * @param newComment - объект с полями для обновления нового комментария
     * @return Comment - DTO обновленного комментария
     */
    @Override
    public Comment update(Integer adId, Integer commentId, CreateOrUpdateComment newComment) {
        AdEntity adEntity = adRepo.findById((long) adId).orElseThrow(() -> new EntityNotFoundException("Объявление id=" + adId + " не найдено"));
        CommentEntity commentEntity = commentRepo.findById((long) commentId).orElseThrow(() -> new EntityNotFoundException("Комментарий не существует"));
        commentEntity.setText(newComment.getText());
        return commentMapper.CommentEntityToComment(commentRepo.save(commentEntity));
    }
}
