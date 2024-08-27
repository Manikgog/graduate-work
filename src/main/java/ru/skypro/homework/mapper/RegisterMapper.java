package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegisterMapper {
    RegisterMapper INSTANCE = Mappers.getMapper( RegisterMapper.class );
    @Mapping(target = "email", source = "username")
    UserEntity toUserEntity(Register source);
}
