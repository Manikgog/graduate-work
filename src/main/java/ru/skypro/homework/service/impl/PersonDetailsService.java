package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entitiy.UserEntity;
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    public PersonDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepo.findByEmail(username);
        if(!user.isPresent()){
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(user.get());
    }
}
