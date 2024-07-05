package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entitiy.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserEntity source);
}
