package ru.skypro.homework.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.config.MyUserDetailsService;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.RegisterMapper;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final MyUserDetailsService myUserDetailService;
    private final PasswordEncoder encoder;
    private final RegisterMapper registerMapper;
    private final UserRepo userRepo;
    private final int MIN_LENGTH_USERNAME = 4;
    private final int MAX_LENGTH_USERNAME = 32;


    public AuthServiceImpl(MyUserDetailsService myUserDetailService, PasswordEncoder encoder, RegisterMapper registerMapper, UserRepo userRepo) {
        this.myUserDetailService = myUserDetailService;
        this.encoder = encoder;
        this.registerMapper = registerMapper;
        this.userRepo = userRepo;
    }

    @Override
    public boolean login(String userName, String password) {
        MyUserDetails userDetails = myUserDetailService.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    public boolean login(Login login) {
        MyUserDetails userDetails = myUserDetailService.loadUserByUsername(login.getUsername());
        return encoder.matches(login.getPassword(), userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        Optional<UserEntity> userEntity = userRepo.findByEmail(register.getUsername());
        if (userEntity.isPresent()) {
            return false;
        }

        UserEntity newUserEntity = registerMapper.toUserEntity(register);
        newUserEntity.setPassword(encoder.encode(register.getPassword()));
        userRepo.save(newUserEntity);
        return true;
    }

}
