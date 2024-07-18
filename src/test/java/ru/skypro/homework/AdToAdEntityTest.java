package ru.skypro.homework;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdToAdEntity;

public class AdToAdEntityTest {

    private final AdToAdEntity adToAdEntity = new AdToAdEntity();
    private final Faker faker = new Faker();

    @Test
    public void perform() {
       UserEntity userEntity = createUser();
       Ad ad = createAd(userEntity);
       AdEntity actual = new AdEntity();

       AdEntity expected = new AdEntity();
       expected.setId(ad.getPk());
       expected.setAuthor(userEntity);
       expected.setImage(ad.getImage());
       expected.setPrice(ad.getPrice());
       expected.setTitle(ad.getTitle());

       adToAdEntity.perform(ad, actual, userEntity);

       Assertions.assertThat(actual).isEqualTo(expected);

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

    public Ad createAd(UserEntity user){
        Ad ad = new Ad();
        ad.setAuthor(user.getId());
        ad.setImage(faker.file().fileName());
        ad.setPk(faker.number().randomNumber());
        ad.setPrice(faker.number().numberBetween(0, 10000));
        ad.setTitle(faker.name().title());
        return ad;
    }

}
