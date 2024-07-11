package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.entitiy.CommentEntity;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "pk",source = "id")
    @Mapping(target = "authorImage", source = "author.image")
    @Mapping(target = "authorFirstName",source = "author.firstName")
    Comment CommentEntityToComment(CommentEntity commentEntity);
}
