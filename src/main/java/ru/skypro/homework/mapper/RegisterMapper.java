package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    @Mapping(target = "email", source = "username")
    UserEntity toUserEntity(Register source);
}
