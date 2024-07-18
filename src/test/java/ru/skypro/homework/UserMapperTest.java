package ru.skypro.homework;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;

public class UserMapperTest {

    private final Faker faker = new Faker();

    @Test
    public void toUser_Test() {
        UserEntity user = createUserEntity();

        User expected = new User();
        expected.setId(user.getId());
        expected.setEmail(user.getEmail());
        expected.setFirstName(user.getFirstName());
        expected.setLastName(user.getLastName());
        expected.setPhone(user.getPhone());
        expected.setRole(user.getRole());
        expected.setImage(user.getImage());

        User actual = UserMapper.INSTANCE.toUser(user);

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public UserEntity createUserEntity() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setPhone(faker.phoneNumber().phoneNumber());
        user.setRole(Role.USER);
        user.setImage(faker.file().fileName());
        user.setPassword(faker.internet().password());
        return user;
    }
}
