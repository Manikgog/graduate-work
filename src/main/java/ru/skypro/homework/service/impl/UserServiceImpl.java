package ru.skypro.homework.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.check.CheckService;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exceptions.EntityNotFoundException;
import ru.skypro.homework.exceptions.WrongPasswordException;
import ru.skypro.homework.mapper.UpdateUserMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.FileManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final UpdateUserMapper updateUserMapper;
    private final PasswordEncoder encoder;
    private final FileManager fileManager;
    private final CheckService checkService;
    private final WebSecurityConfig webSecurityConfig;

    @PostConstruct
    public void prepare() throws IOException {
        Path userImagesFolder = Paths.get(webSecurityConfig.getUserImagesFolder());
        List<String> allFilesList;
        try(Stream<Path> st = Files.list(userImagesFolder)){
            allFilesList = st.map(f -> f.getFileName().toString()).toList();
        }

        List<String> fileNamesFromDB = userRepo.findAll().stream().map(UserEntity::getImage).filter(Objects::nonNull).toList();
        for (String fileName : allFilesList) {
            if(fileNamesFromDB.contains(fileName)){
                continue;
            }
            Path filePath = Paths.get(String.valueOf(userImagesFolder), fileName);
            filePath.toFile().deleteOnExit();
        }
    }

    /**
     * Метод для установки нового пароля для входа
     * @param newPassword - объект содержащий текущий и новый пароли
     * @return boolean - результат смены пароля
     */
    @Override
    public boolean setNewPassword(NewPassword newPassword) {
        log.info("The setNewPassword method of setNewPassword is called");
        MyUserDetails userDetails = getUserDetails();
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
        log.info("The getUser method of setNewPassword is called");
        MyUserDetails userDetails = getUserDetails();
        User user = userMapper.toUser(userDetails.getUser());
        user.setImage("/users/" + user.getId() + "/image");
        return user;
    }

    /**
     * Метод для изменения полей (firstName, lastName, phone) в таблице пользователей в  базе данных
     * @param userPatch - объект с новыми значениями полей
     * @return UpdateUser - объект с обновленными полями
     */
    @Override
    public UpdateUser updateUser(UpdateUser userPatch) {
        log.info("The updateUser method of setNewPassword is called");
        checkService.checkPhone(userPatch.getPhone());
        MyUserDetails userDetails = getUserDetails();
        UserEntity userEntity = userDetails.getUser();
        updateUserMapper.toUserEntity(userPatch, userEntity);
        userRepo.save(userEntity);
        return updateUserMapper.toUpdateUser(userEntity);
    }


    @Override
    public URL getImage(Long id, HttpServletResponse response) throws MalformedURLException {
        log.info("The getPhoto method of UserServiceImpl is called");
        UserEntity userEntity = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id=" + id + " not found"));
        if(userEntity.getImage() == null){
            log.error("The file name is missing in the database");
            throw new EntityNotFoundException("Photo of user not found");
        }
        Path path = Paths.get(webSecurityConfig.getUserImagesFolder(), userEntity.getImage());
        fileManager.getImage(path, response);
        return path.toUri().toURL();
    }

    /**
     * Метод для обновления фотографии пользователя
     * @param image - файл с фотографией пользователя
     * @return User - объект пользователя
     */
    @Override
    public User updateImage(MultipartFile image) throws IOException {
        log.info("The updateImage method of setNewPassword is called");
        MyUserDetails userDetails = getUserDetails();
        UserEntity userEntity = userDetails.getUser();
        String userName = userEntity.getEmail();
        UserEntity userEntityFromDB = userRepo.findByEmail(userName).orElseThrow(() -> new EntityNotFoundException("User with id=" + userName + " not found"));
        String oldImageFileName = userEntityFromDB.getImage();
        UUID uuid = UUID.randomUUID();
        if(oldImageFileName == null || oldImageFileName.isEmpty() || oldImageFileName.isBlank()){
            Path path = fileManager.uploadUserPhoto(uuid.toString(), image);
            String fileName = path.toString().substring(path.toString().lastIndexOf("\\") + 1);
            userEntity.setImage(fileName);
            userEntityFromDB = userRepo.save(userEntity);
            User user = userMapper.toUser(userEntity);
            user.setImage("/users/" + userEntityFromDB.getId() + "/image");
            return user;
        }

        Path path = fileManager.uploadUserPhoto(uuid.toString(), image);
        String fileName = path.toString().substring(path.toString().lastIndexOf("\\") + 1);
        userEntity.setImage(fileName);
        userEntityFromDB = userRepo.save(userEntity);
        User user = userMapper.toUser(userEntity);
        user.setImage("/users/" + userEntityFromDB.getId() + "/image");
        prepare();
        return user;
    }

    /**
     * Метод для получения объекта MyUserDetails из контекста Spring Security
     * @return MeUserDetails
     */
    @Override
    public MyUserDetails getUserDetails() {
        log.info("The getUserDetails method of setNewPassword is called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (MyUserDetails) authentication.getPrincipal();
    }
}
