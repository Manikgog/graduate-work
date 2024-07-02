package ru.skypro.homework.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfiguration {
    private final AuthProviderImpl authProvider;

    public SecurityConfig(AuthProviderImpl authProvider) {
        this.authProvider = authProvider;
    }

    // настраивает аутентификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }
}
