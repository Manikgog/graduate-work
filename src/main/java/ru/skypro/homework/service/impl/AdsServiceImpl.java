package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.check.CheckService;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.constants.Constants;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.exceptions.EntityNotFoundException;
import ru.skypro.homework.mapper.AdEntityToExtendedAdMapper;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.AdToAdEntity;
import ru.skypro.homework.repository.AdRepo;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.FileManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static ru.skypro.homework.constants.Constants.MAX_PRICE;
import static ru.skypro.homework.constants.Constants.MIN_PRICE;

@Slf4j
@Service
@AllArgsConstructor
public class AdsServiceImpl implements AdsService{
    private final AdMapper adMapper;
    private final AdRepo adRepo;
    private final UserService userService;
    private final FileManager fileManager;
    private final CheckService checkService;
    private final Constants constants;
    private final AdEntityToExtendedAdMapper adEntityToExtendedAdMapper = new AdEntityToExtendedAdMapper();
    private final AdToAdEntity adToAdEntity = new AdToAdEntity();

    /**
     * Метод для добавления объявления в базу данных
     * @param createOrUpdateAd - объект с полями для создания нового объевления
     * @param image - файл с изображением
     * @return Ad
     */
    @Override
    public Ad createAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image) {
        log.info("The createAd method of AdsServiceImpl is called");
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


    /**
     * Метод для получения всего списка объявлений
     * @return Ads - объект, содержащий поле с количеством объявлений и их списком
     */
    @Override
    public Ads getAdAll() {
        log.info("The getAdAll method of AdsServiceImpl is called");
        List<Ad> adList = adRepo.findAll().stream().map(adMapper::adEntityToAd).toList();
        Ads ads = new Ads();
        ads.setCount(adList.size());
        ads.setResults(adList);
        return ads;
    }


    /**
     * Метод для обновления полей author, image, pk, price, title объявления
     * @param ad - объект с новыми полями
     * @return Ad - объект обновленными полями из базы данных
     */
    @Override
    public Ad updateAd(Ad ad) {
        log.info("The updateAd method of AdsServiceImpl is called");
        AdEntity adEntity = adRepo.findById(ad.getPk()).orElseThrow(() -> {
            log.error("An EntityNotFoundException " + "(Ad c id=" + ad.getPk() + " not found)" + "exception was thrown when calling the updateAd method of AdsServiceImpl");
            return new EntityNotFoundException("Объявление id=" + ad.getPk() + " не найдено");
        });
        adToAdEntity.perform(ad, adEntity, userService.getUserDetails().getUser());
        return adMapper.adEntityToAd(adRepo.save(adEntity));
    }


    /**
     * Метод для получения объявления по его id
     * @param id - идентификатор объявления
     * @return ExtendedAd - объект объявления с большим количеством полей
     */
    @Override
    public ExtendedAd getAd(Long id) {
        log.info("The getAd method of AdsServiceImpl is called");
        AdEntity adEntity = adRepo.findById(id).orElseThrow(() -> {
            log.error("An EntityNotFoundException " + "(Ad c id=" + id + " not found)" + "exception was thrown when calling the getAd method of AdsServiceImpl");
            return new EntityNotFoundException("Объявление id=" + id + " не найдено");
        });
        return adEntityToExtendedAdMapper.perform(adEntity);
    }


    /**
     * Метод для удаления объявления по его идентификатору
     * @param id - идентификатор
     */
    @Override
    public void deleteAd(Long id) {
        log.info("The deleteAd method of AdsServiceImpl is called");
        adRepo.deleteById(id);
    }

    @Override
    public List<String> updateImage(Long id, MultipartFile image) {
        log.info("The updateImage method of AdsServiceImpl is called");
        AdEntity adEntity = adRepo.findById(id).orElseThrow(() -> {
            log.error("An EntityNotFoundException " + "(Ad c id=" + id + " not found)" + "exception was thrown when calling the updateImage method of AdsServiceImpl");
            return new EntityNotFoundException("Объявление id=" + id + " не найдено");
        });
        MyUserDetails userDetails = userService.getUserDetails();
        Path path = fileManager.uploadAdPhoto(userDetails.getUser().getEmail(), adEntity.getTitle(), image);
        adEntity.setImage(path.toString());
        AdEntity adFromDB = adRepo.save(adEntity);
        List<String> images = new ArrayList<>();
        images.add(adFromDB.getImage());
        return images;
    }

    @Override
    public Ads getAdsAuthorizedUser() {
        log.info("The getAdsAuthorizedUser method of AdsServiceImpl is called");
        MyUserDetails userDetails = userService.getUserDetails();
        List<Ad> adList = adRepo.findByAuthor(userDetails.getUser()).stream().map(adMapper::adEntityToAd).toList();
        Ads ads = new Ads();
        ads.setCount(adList.size());
        ads.setResults(adList);
        return ads;
    }

    @Override
    public Ad updateAds(Long id, CreateOrUpdateAd createOrUpdateAd) {
        log.info("The updateAds method of AdsServiceImpl is called");
        AdEntity adEntity = adRepo.findById(id).orElseThrow(() -> {
            log.error("An EntityNotFoundException " + "(Ad c id=" + id + " not found)" + "exception was thrown when calling the updateAds method of AdsServiceImpl");
            return new EntityNotFoundException("Объявление с id=" + id + " не найдено");
        });
        adMapper.CreateOrUpdateAdToAdEntity(createOrUpdateAd, adEntity);
        return adMapper.adEntityToAd(adRepo.save(adEntity));
    }


}