package ru.skypro.homework.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.dto.*;

import java.net.MalformedURLException;
import java.net.URL;

public interface UserService {
    boolean setNewPassword(NewPassword newPassword);

    User getUser();

    UpdateUser updateUser(UpdateUser userPatch);

    User updateImage(MultipartFile animalPhoto);

    MyUserDetails getUserDetails();

    URL getImage(Long id, HttpServletResponse response) throws MalformedURLException;
}
