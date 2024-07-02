package ru.skypro.homework.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.entitiy.UserEntity;

import java.util.Collection;
import java.util.List;

public class PersonDetails implements UserDetails {
    private final UserEntity userEntity;

    public PersonDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.userEntity.getPassword().toString();
    }

    @Override
    public String getUsername() {
        return this.userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
}
