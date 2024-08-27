package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.check.CheckService;
import ru.skypro.homework.constants.Constants;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.RegisterMapper;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
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
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    /**
     * Метод для аутентификации пользователя
     * @param login - объект, содержащий имя и пароль пользователя
     * @return результат аутентификации
     */
    public boolean login(Login login) {
        log.info("The login method of AuthServiceImpl is called");
        UserDetails userDetails = userDetailsService.loadUserByUsername(login.getUsername());
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
        checkService.checkPhone(register.getPhone());
        UserEntity newUserEntity = registerMapper.toUserEntity(register);
        newUserEntity.setPassword(encoder.encode(register.getPassword()));
        userRepo.save(newUserEntity);
        return true;
    }

}
