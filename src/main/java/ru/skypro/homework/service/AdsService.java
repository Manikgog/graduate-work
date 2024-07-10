package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.util.List;

public interface AdsService {
    Ad createAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image);
    Ads getAdAll();
    Ad updateAd(Ad ad);
    ExtendedAd getAd(int id);
    void deleteAd(int id);
    List<String> updateImage(int id, MultipartFile image);
    Ads getAdsAuthorizedUser();
    Ad updateAds(int id, CreateOrUpdateAd createOrUpdateAd);
}