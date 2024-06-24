package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

public interface UserService {
    boolean setNewPassword(NewPassword newPassword);

    User getUser();

    UserPatch updateUser(UserPatch userPatch);

    User updateImage(MultipartFile animalPhoto);

    UserRegister registerUser(UserRegister userRegister);

    void loginUser(Login login);
}
