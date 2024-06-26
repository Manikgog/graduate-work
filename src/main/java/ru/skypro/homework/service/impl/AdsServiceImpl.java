package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;

import java.util.List;

@Service
public class AdsServiceImpl implements AdsService{

    @Override
    public Ad createAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile images) {
        return null;
    }

    @Override
    public List<Ad> getAdAll() {
        return List.of();
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
}
