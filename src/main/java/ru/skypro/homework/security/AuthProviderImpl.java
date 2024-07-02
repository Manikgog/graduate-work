package ru.skypro.homework.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.skypro.homework.service.impl.PersonDetailsService;

import java.util.Collections;

@Component
public class AuthProviderImpl implements AuthenticationProvider {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public AuthProviderImpl(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        UserDetails userDetails = personDetailsService.loadUserByUsername(username);
        Integer password = authentication.getCredentials().toString().hashCode();
        if(!password.toString().equals(userDetails.getPassword())){
            throw new BadCredentialsException("Incorrect password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}








