package ru.skypro.homework.config;

import org.springframework.security.test.context.support.WithSecurityContext;
import ru.skypro.homework.dto.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomSecurityContextFactory.class)
public @interface WithCustomUser {

    String username();
    String password();
    Role role();
}
