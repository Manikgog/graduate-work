package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.config.MyUserDetailsService;
import ru.skypro.homework.constants.Constants;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.RegisterMapper;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.check.CheckService;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MyUserDetailsService myUserDetailService;
    private final PasswordEncoder encoder;
    private final RegisterMapper registerMapper;
    private final UserRepo userRepo;
    private final Constants constants;
    private final CheckService checkService;

    /**
     * Метод для аутентификации пользователя
     * @param userName - имя пользователя (email)
     * @param password - пароль
     * @return результат аутентификации
     */
    @Override
    public boolean login(String userName, String password) {
        log.info("The login method of AuthServiceImpl is called");
        MyUserDetails userDetails = myUserDetailService.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    /**
     * Метод для аутентификации пользователя
     * @param login - объект, содержащий имя и пароль пользователя
     * @return результат аутентификации
     */
    public boolean login(Login login) {
        log.info("The login method of AuthServiceImpl is called");
        MyUserDetails userDetails = myUserDetailService.loadUserByUsername(login.getUsername());
        return encoder.matches(login.getPassword(), userDetails.getPassword());
    }

    /**
     * Метод для регистрации пользователя
     * @param register - объект, содержащий поля, необходимые для регистрации пользователя
     * @return результат регистрации
     */
    @Override
    public boolean register(Register register) {
        log.info("The register method of AuthServiceImpl is called");
        Optional<UserEntity> userEntity = userRepo.findByEmail(register.getUsername());
        if (userEntity.isPresent()) {
            return false;
        }
        checkService.checkString(constants.MIN_LENGTH_USERNAME, constants.MAX_LENGTH_USERNAME, register.getUsername());
        checkService.checkString(constants.MIN_LENGTH_PASSWORD, constants.MAX_LENGTH_PASSWORD, register.getPassword());
        checkService.checkString(constants.MIN_LENGTH_FIRSTNAME, constants.MAX_LENGTH_FIRSTNAME, register.getFirstName());
        checkService.checkString(constants.MIN_LENGTH_LASTNAME, constants.MAX_LENGTH_LASTNAME, register.getLastName());
        checkService.checkPhone(constants.PHONE_PATTERN, register.getPhone());
        checkService.checkRole(register.getRole());
        UserEntity newUserEntity = registerMapper.toUserEntity(register);
        newUserEntity.setPassword(encoder.encode(register.getPassword()));
        userRepo.save(newUserEntity);
        return true;
    }

}
