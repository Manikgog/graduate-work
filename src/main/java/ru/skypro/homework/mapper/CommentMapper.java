package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.entity.CommentEntity;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper( CommentMapper.class );
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "pk",source = "id")
    @Mapping(target = "authorImage", source = "author.image")
    @Mapping(target = "authorFirstName",source = "author.firstName")
    Comment commentEntityToComment(CommentEntity commentEntity);
}
