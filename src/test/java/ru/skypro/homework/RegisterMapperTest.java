package ru.skypro.homework;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.RegisterMapper;

public class RegisterMapperTest {

    private final Faker faker = new Faker();

    @Test
    public void toUserEntity() {
        Register register = createRegister();

        UserEntity expected = new UserEntity();
        expected.setEmail(register.getUsername());
        expected.setFirstName(register.getFirstName());
        expected.setLastName(register.getLastName());
        expected.setPhone(register.getPhone());
        expected.setRole(register.getRole());
        expected.setPassword(register.getPassword());

        UserEntity actual = RegisterMapper.INSTANCE.toUserEntity(register);

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public Register createRegister() {
        Register register = new Register();
        register.setUsername(faker.internet().emailAddress());
        register.setPassword(faker.internet().password());
        register.setFirstName(faker.name().firstName());
        register.setLastName(faker.name().lastName());
        register.setPhone(faker.phoneNumber().phoneNumber());
        register.setRole(Role.USER);
        return register;
    }
}
