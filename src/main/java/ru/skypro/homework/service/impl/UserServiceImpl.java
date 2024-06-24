package ru.skypro.homework.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public boolean setNewPassword(NewPassword newPassword) {
        return true;
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public UserPatch updateUser(UserPatch userPatch) {
        return null;
    }

    @Override
    public User updateImage(MultipartFile animalPhoto) {
        return null;
    }

    @Override
    public UserRegister registerUser(UserRegister userRegister) {
        return null;
    }

    @Override
    public void loginUser(UserAuthorisation userAuthorisation) {

    }
}
