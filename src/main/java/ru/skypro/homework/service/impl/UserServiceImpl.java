package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entitiy.UserEntity;
import ru.skypro.homework.mapper.UpdateUserMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.security.PersonDetails;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final UpdateUserMapper updateUserMapper;

    @Override
    public boolean setNewPassword(NewPassword newPassword) {
        return true;
    }

    @Override
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return userMapper.toUser(personDetails.getUserEntity());
    }

    @Override
    public UpdateUser updateUser(UpdateUser userPatch) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        UserEntity userEntity = personDetails.getUserEntity();
        UserEntity fromDB = userRepo.findByEmail(userEntity.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        fromDB.setFirstName(userPatch.getFirstName());
        fromDB.setLastName(userPatch.getLastName());
        fromDB.setPhone(userPatch.getPhone());
        userRepo.save(fromDB);
        UpdateUser updateUser = updateUserMapper.toUpdateUser(fromDB);
        return updateUser;
    }

    @Override
    public User updateImage(MultipartFile animalPhoto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john@doe.com");
        userEntity.setId(1L);
        userEntity.setImage("/images/user.png");
        userEntity.setPassword(123);
        userEntity.setPhone("+7-950-540-90-61");
        userEntity.setRole(Role.USER);
        return userMapper.toUser(userEntity);
    }

    @Override
    public Register registerUser(Register userRegister) {

        return null;
    }

    @Override
    public void loginUser(Login login) {

    }
}
