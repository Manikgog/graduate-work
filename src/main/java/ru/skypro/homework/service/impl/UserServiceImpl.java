package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mappers.UpdateUserMapper;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final UpdateUserMapper updateUserMapper;

    public UserServiceImpl(UserRepo userRepo, UserMapper userMapper, UpdateUserMapper updateUserMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.updateUserMapper = updateUserMapper;
    }

    @Override
    public boolean setNewPassword(NewPassword newPassword) {
        return true;
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public UpdateUser updateUser(UpdateUser userPatch) {
        return null;
    }

    @Override
    public User updateImage(MultipartFile animalPhoto) {
        return null;
    }

    @Override
    public Register registerUser(Register userRegister) {
        return null;
    }

    @Override
    public void loginUser(Login login) {

    }
}
