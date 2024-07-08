package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UpdateUserMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.service.CheckService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.FileManager;

import java.nio.file.Path;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final UpdateUserMapper updateUserMapper;
    private final PasswordEncoder encoder;
    private final FileManager fileManager;
    private final CheckService checkService;
    private final int MIN_PASSWORD_LENGTH = 8;
    private final int MAX_PASSWORD_LENGTH = 16;


    @Override
    public boolean setNewPassword(NewPassword newPassword) {
        checkService.checkString(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH, newPassword.getCurrentPassword());
        checkService.checkString(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH, newPassword.getNewPassword());
        MyUserDetails userDetails = getUserDetails();
        UserEntity userEntity = userDetails.getUser();

        if(encoder.matches(newPassword.getCurrentPassword(), userEntity.getPassword())) {
            userEntity.setPassword(encoder.encode(newPassword.getNewPassword()));
            userRepo.save(userEntity);
            return true;
        }
        return false;
    }

    @Override
    public User getUser() {
        MyUserDetails userDetails = getUserDetails();
        return userMapper.toUser(userDetails.getUser());
    }

    @Override
    public UpdateUser updateUser(UpdateUser userPatch) {
        MyUserDetails userDetails = getUserDetails();
        UserEntity userEntity = userDetails.getUser();
        updateUserMapper.toUserEntity(userPatch, userEntity);
        userRepo.save(userEntity);
        return updateUserMapper.toUpdateUser(userEntity);
    }

    @Override
    public User updateImage(MultipartFile photo) {
        MyUserDetails userDetails = getUserDetails();
        Path path = fileManager.uploadUserPhoto(userDetails.getUsername(), photo);
        UserEntity userEntity = userDetails.getUser();
        userEntity.setImage(path.toString());
        userRepo.save(userEntity);
        return userMapper.toUser(userEntity);
    }

    @Override
    public Register registerUser(Register userRegister) {
        return null;
    }

    @Override
    public MyUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (MyUserDetails) authentication.getPrincipal();
    }


    @Override
    public void loginUser(Login login) {

    }
}
