package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {
    Comments get(long id);

    Comment create(long id, CreateOrUpdateComment newComment);

    void delete(long commentId);

    Comment update(long commentId, CreateOrUpdateComment newComment);
}
