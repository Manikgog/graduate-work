package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.entitiy.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UpdateUserMapper {
    UpdateUser toUpdateUser(UserEntity source);
}

