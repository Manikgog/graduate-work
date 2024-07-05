package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.entitiy.UserEntity;

@Mapper(componentModel = "spring")
public interface UpdateUserMapper {
    UpdateUser toUpdateUser(UserEntity source);
}

