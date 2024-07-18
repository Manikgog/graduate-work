package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UpdateUserMapper {
    UpdateUserMapper INSTANCE = Mappers.getMapper( UpdateUserMapper.class );
    UpdateUser toUpdateUser(UserEntity source);

    void toUserEntity(UpdateUser source, @MappingTarget UserEntity target);
}
