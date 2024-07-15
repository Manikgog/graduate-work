package ru.skypro.homework.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.check.CheckService;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.config.WebSecurityConfig;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private final AdEntityToExtendedAdMapper adEntityToExtendedAdMapper;
    private final AdToAdEntity adToAdEntity;
    private final WebSecurityConfig webSecurityConfig;

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
            Path path = fileManager.uploadAdPhoto(webSecurityConfig.getUuid().toString(), image);
            String fileName = path.toString().substring(path.toString().lastIndexOf("\\") + 1);
            newAd.setImage(fileName);
            AdEntity adFromDB = adRepo.save(newAd);
            Ad ad = adMapper.adEntityToAd(adFromDB);
            ad.setImage("/ads/" + ad.getPk() + "/image");
            return ad;
        }
        AdEntity adFromDB = adRepo.save(newAd);
        Ad ad = adMapper.adEntityToAd(adFromDB);
        ad.setImage("/ads/" + ad.getPk() + "/image");
        return ad;
    }


    /**
     * Метод для получения всего списка объявлений
     * @return Ads - объект, содержащий поле с количеством объявлений и их списком
     */
    @Override
    public Ads getAdAll() {
        log.info("The getAdAll method of AdsServiceImpl is called");
        List<Ad> adList = adRepo.findAll().stream().map(adMapper::adEntityToAd).toList();
        for(Ad ad : adList){
            ad.setImage("/ads/" + ad.getPk() + "/image");
        }
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
        Ad newAd = adMapper.adEntityToAd(adRepo.save(adEntity));
        newAd.setImage("/ads/" + ad.getPk() + "/image");
        return newAd;
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
        ExtendedAd extendedAd = adEntityToExtendedAdMapper.perform(adEntity);
        extendedAd.setImage("/ads/" + extendedAd.getPk() + "/image");
        return extendedAd;
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
        String oldImageFileName = adEntity.getImage();
        if(oldImageFileName.isEmpty() || oldImageFileName.isBlank()){
            UUID uuid = webSecurityConfig.getUuid();
            Path path = fileManager.uploadAdPhoto(uuid.toString(), image);
            String fileName = path.toString().substring(path.toString().lastIndexOf("\\") + 1);
            adEntity.setImage(fileName);
            AdEntity adFromDB = adRepo.save(adEntity);
            adFromDB.setImage("/ads/" + adFromDB.getId() + "/image");
            List<String> images = new ArrayList<>();
            images.add(adFromDB.getImage());
            return images;
        }
        Path pathToOldImage = Paths.get(webSecurityConfig.getAdImagesFolder(), oldImageFileName);
        Path pathToNewImage;
        if(Files.exists(pathToOldImage)){
            pathToNewImage = fileManager.uploadAdPhoto(oldImageFileName.substring(0, oldImageFileName.indexOf('.')), image);
        }else {
            UUID uuid = webSecurityConfig.getUuid();
            pathToNewImage = fileManager.uploadAdPhoto(uuid.toString(), image);
        }
        String fileName = pathToNewImage.toString().substring(pathToNewImage.toString().lastIndexOf("\\") + 1);
        adEntity.setImage(fileName);
        AdEntity adFromDB = adRepo.save(adEntity);
        adFromDB.setImage("/ads/" + adFromDB.getId() + "/image");
        List<String> images = new ArrayList<>();
        images.add(adFromDB.getImage());
        return images;
    }

    @Override
    public Ads getAdsAuthorizedUser() {
        log.info("The getAdsAuthorizedUser method of AdsServiceImpl is called");
        MyUserDetails userDetails = userService.getUserDetails();
        List<Ad> adList = adRepo.findByAuthor(userDetails.getUser()).stream().map(adMapper::adEntityToAd).toList();
        for(Ad ad : adList){
            ad.setImage("/ads/" + ad.getPk() + "/image");
        }
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
        adMapper.createOrUpdateAdToAdEntity(createOrUpdateAd, adEntity);
        Ad ad = adMapper.adEntityToAd(adRepo.save(adEntity));
        ad.setImage("/ads/" + ad.getPk() + "/image");
        return ad;
    }


    @Override
    public URL getImage(Long id, HttpServletResponse response) throws MalformedURLException {
        log.info("The getImage method of AdsServiceImpl is called");
        AdEntity adEntity = adRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Ad with id=" + id + " not found"));
        Path path = Paths.get(webSecurityConfig.getAdImagesFolder(), adEntity.getImage());
        fileManager.getImage(path, response);
        return path.toUri().toURL();
    }
}