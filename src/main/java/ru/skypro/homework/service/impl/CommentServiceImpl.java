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
    public Comments get(Long adId) {
        List<Comment> commentList = commentRepo.findByAdId(adId)
                .stream()
                .map(commentMapper::commentEntityToComment)
                .toList();
        for(Comment item : commentList){
            item.setAuthorImage("/users/" + item.getAuthor() + "/image");
        }
        Comments comments = new Comments();
        comments.setCount(commentList.size());
        comments.setResults(commentList);
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
    public Comment create(Long adId, CreateOrUpdateComment newComment) {
        AdEntity adEntity = adRepo.findById(adId).orElseThrow(() -> {
            log.info("The create method of CommentServiceImpl is called");
            return new EntityNotFoundException("Объявление id=" + adId + " не найдено");
        });
        CommentEntity commentEntity = new CommentEntity();
        MyUserDetails myUserDetails = userService.getUserDetails();
        commentEntity.setAuthor(myUserDetails.getUser());
        commentEntity.setAd(adEntity);
        commentEntity.setCreatedAt(Instant.now().toEpochMilli());
        commentEntity.setText(newComment.getText());
        CommentEntity commentEntityFromDB = commentRepo.save(commentEntity);
        Comment comment = commentMapper.commentEntityToComment(commentEntity);
        comment.setAuthorImage("/users/" + commentEntityFromDB.getAuthor().getId() + "/image");
        return comment;
    }

    /**
     * Метод для удаления комментария к объявлению
     *
     * @param adId      - идентификатор объявления
     * @param commentId - идентификатор комментария
     */
    @Override
    public void delete(Long adId, Long commentId) {
        commentRepo.deleteById(commentId);

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
    public Comment update(Long adId, Long commentId, CreateOrUpdateComment newComment) {
        CommentEntity commentEntity = commentRepo.findById(commentId).orElseThrow();
        commentEntity.setText(newComment.getText());
        return commentMapper.commentEntityToComment(commentRepo.save(commentEntity));
    }
}
