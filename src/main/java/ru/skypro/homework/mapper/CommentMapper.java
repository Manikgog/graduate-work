package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.entitiy.CommentEntity;


@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "pk",source = "ad.id")
    Comment CommentEntityToComment(CommentEntity commentEntity);
}
