package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {
    Comments get(Long id);

    Comment create(Long id, CreateOrUpdateComment newComment);

    void delete(Long adId, Long commentId);

    Comment update(Long adId, Long commentId, CreateOrUpdateComment newComment);
}
