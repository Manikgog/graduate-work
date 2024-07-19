package ru.skypro.homework.mapper;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;


public class AdMapperTest {

    @Test
    public void adEntityToAd_Test() {
        AdEntityToExtendedAdMapperTest adEntityToExtendedAdMapperTest = new AdEntityToExtendedAdMapperTest();
        UserEntity user = adEntityToExtendedAdMapperTest.createUser();
        AdEntity adEntity = adEntityToExtendedAdMapperTest.createAd(user);
        Ad expected = createAd(adEntity);
        Ad actual = AdMapper.INSTANCE.adEntityToAd(adEntity);

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void adEntityToExtendedAd_Test(){
        AdEntityToExtendedAdMapperTest adEntityToExtendedAdMapperTest = new AdEntityToExtendedAdMapperTest();
        UserEntity user = adEntityToExtendedAdMapperTest.createUser();
        AdEntity adEntity = adEntityToExtendedAdMapperTest.createAd(user);
        ExtendedAd expected = adEntityToExtendedAdMapperTest.createExtendedAd(adEntity);

        ExtendedAd actual = AdMapper.INSTANCE.adEntityToExtendedAd(adEntity);

        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void createOrUpdateAdToAdEntity_Test(){
        AdEntityToExtendedAdMapperTest adEntityToExtendedAdMapperTest = new AdEntityToExtendedAdMapperTest();
        UserEntity user = adEntityToExtendedAdMapperTest.createUser();
        AdEntity adEntity = adEntityToExtendedAdMapperTest.createAd(user);
        CreateOrUpdateAd createOrUpdateAd = createCreateOrUpdateAd(adEntity);

        AdEntity expected = adEntity;
        AdEntity actual = new AdEntity();
        AdMapper.INSTANCE.createOrUpdateAdToAdEntity(createOrUpdateAd, actual);

        Assertions.assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        Assertions.assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
    }

    public Ad createAd(AdEntity adEntity){
        Ad ad = new Ad();
        ad.setAuthor(adEntity.getAuthor().getId());
        ad.setImage(adEntity.getImage());
        ad.setPk(adEntity.getId());
        ad.setPrice(adEntity.getPrice());
        ad.setTitle(adEntity.getTitle());
        return ad;
    }

    public CreateOrUpdateAd createCreateOrUpdateAd(AdEntity adEntity){
        CreateOrUpdateAd createAd = new CreateOrUpdateAd();
        createAd.setTitle(adEntity.getTitle());
        createAd.setPrice(adEntity.getPrice());
        createAd.setDescription(adEntity.getDescription());
        return createAd;
    }

}
