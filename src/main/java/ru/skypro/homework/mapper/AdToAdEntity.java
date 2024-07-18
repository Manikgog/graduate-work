package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

@Component
public class AdToAdEntity {

    public void perform(Ad ad, AdEntity adEntity, UserEntity userEntity) {
        adEntity.setId(ad.getPk());
        adEntity.setAuthor(userEntity);
        adEntity.setImage(ad.getImage());
        adEntity.setPrice(ad.getPrice());
        adEntity.setTitle(ad.getTitle());
    }
}
