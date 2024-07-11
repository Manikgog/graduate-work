package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {
    Comments get(Integer id);

    Comment create(Integer id, CreateOrUpdateComment newComment);

    void delete(Integer adId, Integer commentId);

    Comment update(Integer adId, Integer commentId, CreateOrUpdateComment newComment);
}
