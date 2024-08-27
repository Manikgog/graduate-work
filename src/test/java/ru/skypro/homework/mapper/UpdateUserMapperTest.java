package ru.skypro.homework.mapper;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.entity.UserEntity;

public class UpdateUserMapperTest {
    private final Faker faker = new Faker();

    @Test
    public void toUpdateUser_Test() {
        UserEntity user = createUserEntity();

        UpdateUser expected = new UpdateUser();
        expected.setFirstName(user.getFirstName());
        expected.setLastName(user.getLastName());
        expected.setPhone(user.getPhone());

        UpdateUser actual = UpdateUserMapper.INSTANCE.toUpdateUser(user);

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void toUserEntity_Test() {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName(faker.name().firstName());
        updateUser.setLastName(faker.name().lastName());
        updateUser.setPhone(faker.phoneNumber().phoneNumber());

        UserEntity expected = new UserEntity();
        expected.setFirstName(updateUser.getFirstName());
        expected.setLastName(updateUser.getLastName());
        expected.setPhone(updateUser.getPhone());

        UserEntity actual = new UserEntity();

        UpdateUserMapper.INSTANCE.toUserEntity(updateUser, actual);

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
