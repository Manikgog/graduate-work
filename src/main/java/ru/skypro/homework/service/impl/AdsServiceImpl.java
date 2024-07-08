package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepo;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CheckService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.FileManager;

import java.nio.file.Path;
import java.util.List;

@Service
@AllArgsConstructor
public class AdsServiceImpl implements AdsService{
    private final AdMapper adMapper;
    private final AdRepo adRepo;
    private final UserService userService;
    private final FileManager fileManager;
    private final CheckService checkService;
    private final int MIN_LENGTH_TITLE_AD = 4;
    private final int MAX_LENGTH_TITLE_AD = 32;
    private final int MIN_LENGTH_DESCRIPTION_AD = 8;
    private final int MAX_LENGTH_DESCRIPTION_AD = 64;
    private final int MIN_PRICE = 0;
    private final int MAX_PRICE = 10_000_000;

    /**
     * Метод для добавления объявления в базу данных
     * @param createOrUpdateAd - объект с полями для создания нового объевления
     * @param image
     * @return
     */
    @Override
    public Ad createAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image) {
        checkService.checkString(MIN_LENGTH_TITLE_AD, MAX_LENGTH_TITLE_AD, createOrUpdateAd.getTitle());
        checkService.checkString(MIN_LENGTH_DESCRIPTION_AD, MAX_LENGTH_DESCRIPTION_AD, createOrUpdateAd.getDescription());
        checkService.checkNumber(MIN_PRICE, MAX_PRICE, createOrUpdateAd.getPrice());

        MyUserDetails userDetails = userService.getUserDetails();
        AdEntity newAd = new AdEntity();
        newAd.setTitle(createOrUpdateAd.getTitle());
        newAd.setAuthor(userDetails.getUser());
        newAd.setPrice(createOrUpdateAd.getPrice());
        newAd.setDescription(createOrUpdateAd.getDescription());
        if(!image.isEmpty()){
            Path path = fileManager.uploadAdPhoto(userDetails.getUser().getEmail(), createOrUpdateAd.getTitle(), image);
            newAd.setImage(path.toString());
            AdEntity adFromDB = adRepo.save(newAd);
            return adMapper.adEntityToAd(adFromDB);
        }
        AdEntity adFromDB = adRepo.save(newAd);
        return adMapper.adEntityToAd(adFromDB);
    }

    @Override
    public List<Ad> getAdAll() {
        return null;
    }

    @Override
    public Ad updateAd(Ad ad) {
        return null;
    }

    @Override
    public ExtendedAd getAd(int id) {
        return null;
    }

    @Override
    public void deleteAd(int id) {
    }

    @Override
    public List<String> updateImage(int id, MultipartFile image) {
        return null;
    }

    @Override
    public Comments getAdsAuthorizedUser(int id, CreateOrUpdateAd createOrUpdateAd) {
        return null;
    }

    @Override
    public Ad updateAds() {
        return null;
    }
}