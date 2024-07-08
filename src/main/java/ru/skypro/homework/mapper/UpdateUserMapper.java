package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UpdateUserMapper {
    UpdateUser toUpdateUser(UserEntity source);

    void toUserEntity(UpdateUser source, @MappingTarget UserEntity target);
}

