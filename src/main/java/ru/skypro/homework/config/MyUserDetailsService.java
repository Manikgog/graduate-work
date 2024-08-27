package ru.skypro.homework.config;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exceptions.EntityNotFoundException;
import ru.skypro.homework.repository.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public MyUserDetails loadUserByUsername(String username) {
        UserEntity user = userRepo.findByEmail(username).orElseThrow(()-> new EntityNotFoundException("User is not found"));
        return new MyUserDetails(user);
    }
}
