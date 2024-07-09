package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.constants.Constants;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exceptions.WrongPasswordException;
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
    private final Constants constants;

    /**
     * Метод для установки нового пароля для входа
     * @param newPassword - объект содержащий текущий и новый пароли
     * @return boolean - результат смены пароля
     */
    @Override
    public boolean setNewPassword(NewPassword newPassword) {
        MyUserDetails userDetails = getUserDetails();
        checkService.checkString(constants.MIN_LENGTH_PASSWORD, constants.MAX_LENGTH_PASSWORD, newPassword.getNewPassword());
        UserEntity userEntity = userDetails.getUser();

        if(encoder.matches(newPassword.getCurrentPassword(), userEntity.getPassword())) {
            userEntity.setPassword(encoder.encode(newPassword.getNewPassword()));
            userRepo.save(userEntity);
            return true;
        }
        throw new WrongPasswordException("Введён некорректный текущий пароль");
    }

    /**
     * Метод для получения текущего пользователя
     * @return User - объект пользователя
     */
    @Override
    public User getUser() {
        MyUserDetails userDetails = getUserDetails();
        return userMapper.toUser(userDetails.getUser());
    }

    /**
     * Метод для изменения полей (firstName, lastName, phone) в таблице пользователей в  базе данных
     * @param userPatch - объект с новыми значениями полей
     * @return UpdateUser - объект с обновленными полями
     */
    @Override
    public UpdateUser updateUser(UpdateUser userPatch) {
        checkService.checkString(constants.MIN_LENGTH_FIRSTNAME, constants.MAX_LENGTH_FIRSTNAME, userPatch.getFirstName());
        checkService.checkString(constants.MIN_LENGTH_LASTNAME, constants.MAX_LENGTH_LASTNAME, userPatch.getLastName());
        checkService.checkPhone(constants.PHONE_PATTERN, userPatch.getPhone());
        MyUserDetails userDetails = getUserDetails();
        UserEntity userEntity = userDetails.getUser();
        updateUserMapper.toUserEntity(userPatch, userEntity);
        userRepo.save(userEntity);
        return updateUserMapper.toUpdateUser(userEntity);
    }


    /**
     * Метод для обновления фотографии пользователя
     * @param photo - файл с фотографией пользователя
     * @return User - объект пользователя
     */
    @Override
    public User updateImage(MultipartFile photo) {
        MyUserDetails userDetails = getUserDetails();
        Path path = fileManager.uploadUserPhoto(userDetails.getUsername(), photo);
        UserEntity userEntity = userDetails.getUser();
        userEntity.setImage("\\" + path.toString());
        userRepo.save(userEntity);
        return userMapper.toUser(userEntity);
    }

    /**
     * Метод для получения объекта MyUserDetails из контекста Spring Security
     * @return MeUserDetails
     */
    @Override
    public MyUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (MyUserDetails) authentication.getPrincipal();
    }
}
