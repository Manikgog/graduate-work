package ru.skypro.homework.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public interface AdsService {
    Ad createAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image);
    Ads getAdAll();
    Ad updateAd(Ad ad);
    ExtendedAd getAd(Long id);
    void deleteAd(Long id);
    List<String> updateImage(Long id, MultipartFile image);
    Ads getAdsAuthorizedUser();
    Ad updateAds(Long id, CreateOrUpdateAd createOrUpdateAd);

    URL getImage(Long id, HttpServletResponse response) throws MalformedURLException;
}