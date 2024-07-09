package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import java.util.List;

public interface AdsService {
    Ad createAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image);
    Ads getAdAll();
    Ad updateAd(Ad ad);
    ExtendedAd getAd(int id);
    void deleteAd(int id);
    List<String> updateImage(int id, MultipartFile image);
    Comments getAdsAuthorizedUser(int id, CreateOrUpdateAd createOrUpdateAd);
    Ad updateAds();
}