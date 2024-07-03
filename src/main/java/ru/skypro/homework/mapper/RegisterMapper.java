package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entitiy.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegisterMapper {
    @Mapping(target = "email", source = "username")
    UserEntity toUserEntity(Register source);
}
