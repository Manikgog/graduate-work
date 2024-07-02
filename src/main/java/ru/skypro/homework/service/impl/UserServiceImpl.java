package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entitiy.UserEntity;
import ru.skypro.homework.mapper.UpdateUserMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final UpdateUserMapper updateUserMapper;

    @Override
    public boolean setNewPassword(NewPassword newPassword) {
        return true;
    }

    @Override
    public User getUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john@doe.com");
        userEntity.setId(1L);
        userEntity.setImage("/images/user.png");
        userEntity.setPassword(123);
        userEntity.setPhone("+7-950-540-90-61");
        userEntity.setRole(Role.USER);
        return userMapper.toUser(userEntity);
    }

    @Override
    public UpdateUser updateUser(UpdateUser userPatch) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john@doe.com");
        userEntity.setId(1L);
        userEntity.setImage("/images/user.png");
        userEntity.setPassword(123);
        userEntity.setPhone("+7-950-540-90-61");
        userEntity.setRole(Role.USER);
        UpdateUser updateUser = updateUserMapper.toUpdateUser(userEntity);
        return updateUser;
    }

    @Override
    public User updateImage(MultipartFile animalPhoto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john@doe.com");
        userEntity.setId(1L);
        userEntity.setImage("/images/user.png");
        userEntity.setPassword(123);
        userEntity.setPhone("+7-950-540-90-61");
        userEntity.setRole(Role.USER);
        return userMapper.toUser(userEntity);
    }

    @Override
    public Register registerUser(Register userRegister) {

        return null;
    }

    @Override
    public void loginUser(Login login) {

    }
}
