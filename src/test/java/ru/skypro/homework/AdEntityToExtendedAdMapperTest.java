package ru.skypro.homework;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdEntityToExtendedAdMapper;

public class AdEntityToExtendedAdMapperTest {

    private final AdEntityToExtendedAdMapper mapper = new AdEntityToExtendedAdMapper();
    private final Faker faker = new Faker();
    @Test
    public void perform_Test() {
        UserEntity user = createUser();
        AdEntity adEntity = createAd(user);
        ExtendedAd expected = createExtendedAd(adEntity);

        ExtendedAd actual = mapper.perform(adEntity);

        Assertions.assertThat(expected)
                .usingRecursiveComparison()
                .isEqualTo(actual);
    }

    public UserEntity createUser() {
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

    public AdEntity createAd(UserEntity user) {
        AdEntity adEntity = new AdEntity();
        adEntity.setId(1L);
        adEntity.setAuthor(user);
        adEntity.setImage(faker.file().fileName());
        adEntity.setPrice(faker.number().numberBetween(1, 10000));
        adEntity.setTitle(faker.book().title());
        adEntity.setDescription(faker.expression(adEntity.getTitle()));
        return adEntity;
    }

    public ExtendedAd createExtendedAd(AdEntity adEntity) {
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(adEntity.getId());
        extendedAd.setAuthorFirstName(adEntity.getAuthor().getFirstName());
        extendedAd.setAuthorLastName(adEntity.getAuthor().getLastName());
        extendedAd.setDescription(adEntity.getDescription());
        extendedAd.setEmail(adEntity.getAuthor().getEmail());
        extendedAd.setImage(adEntity.getImage());
        extendedAd.setPhone(adEntity.getAuthor().getPhone());
        extendedAd.setPrice(adEntity.getPrice());
        extendedAd.setTitle(adEntity.getTitle());
        return extendedAd;
    }
}
