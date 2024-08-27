package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

@Component
public class AdEntityToExtendedAdMapper implements Mapper<AdEntity, ExtendedAd> {

    @Override
    public ExtendedAd perform(AdEntity adEntity) {
        ExtendedAd extendedAd = new ExtendedAd();
        UserEntity userEntity = adEntity.getAuthor();
        extendedAd.setPk(adEntity.getId());
        extendedAd.setAuthorFirstName(userEntity.getFirstName());
        extendedAd.setAuthorLastName(userEntity.getLastName());
        extendedAd.setDescription(adEntity.getDescription());
        extendedAd.setEmail(userEntity.getEmail());
        extendedAd.setImage(adEntity.getImage());
        extendedAd.setPhone(userEntity.getPhone());
        extendedAd.setPrice(adEntity.getPrice());
        extendedAd.setTitle(adEntity.getTitle());
        return extendedAd;
    }
}
