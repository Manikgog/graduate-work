package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
import ru.skypro.homework.repository.UserRepo;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CheckService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.FileManager;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AdsServiceImpl implements AdsService{
    private final AdMapper adMapper;
    private final AdRepo adRepo;
    private final UserRepo userRepo;
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
        checkService.checkString(constants.MIN_LENGTH_TITLE_AD, constants.MAX_LENGTH_TITLE_AD, createOrUpdateAd.getTitle());
        checkService.checkString(constants.MIN_LENGTH_DESCRIPTION_AD, constants.MAX_LENGTH_DESCRIPTION_AD, createOrUpdateAd.getDescription());
        checkService.checkNumber(constants.MIN_PRICE, constants.MAX_PRICE, createOrUpdateAd.getPrice());

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
        List<Ad> adList = adRepo.findAll().stream().map(adMapper::adEntityToAd).toList();;
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
        AdEntity adEntity = adRepo.findById(ad.getPk()).orElseThrow(() -> new EntityNotFoundException("Объявление id=" + ad.getPk() + " не найдено"));
        adToAdEntity.perform(ad, adEntity, userService.getUserDetails().getUser());
        return adMapper.adEntityToAd(adRepo.save(adEntity));
    }


    /**
     * Метод для получения объявления по его id
     * @param id - идентификатор объявления
     * @return ExtendedAd - объект объявления с большим количеством полей
     */
    @Override
    public ExtendedAd getAd(int id) {
        AdEntity adEntity = adRepo.findById((long)id).orElseThrow(() -> new EntityNotFoundException("Объявление id=" + id + " не найдено"));
        return adEntityToExtendedAdMapper.perform(adEntity);
    }


    /**
     * Метод для удаления объявления по его идентификатору
     * @param id - идентификатор
     */
    @Override
    @PostAuthorize("hasRole('ADMIN') || userService.getUserDetails().getUser().getEmail() == autentication.name")
    public void deleteAd(int id) {
        adRepo.deleteById((long)id);
    }

    @Override
    public String[] updateImage(int id, MultipartFile image) {
        AdEntity adEntity = adRepo.findById((long)id).orElseThrow(() -> new EntityNotFoundException("Объявление id=" + id + " не найдено"));
        MyUserDetails userDetails = userService.getUserDetails();
        Path path = fileManager.uploadAdPhoto(userDetails.getUser().getEmail(), adEntity.getTitle(), image);
        adEntity.setImage(path.toString());
        AdEntity adFromDB = adRepo.save(adEntity);
        List<String> images = new ArrayList<>();
        images.add(path.toString());
        return images.toArray(new String[images.size()]);
    }

    @Override
    public Ads getAdsAuthorizedUser() {
        MyUserDetails userDetails = userService.getUserDetails();
        List<Ad> adList = adRepo.findByAuthor(userDetails.getUser()).stream().map(adMapper::adEntityToAd).toList();
        Ads ads = new Ads();
        ads.setCount(adList.size());
        ads.setResults(adList);
        return ads;
    }

    @Override
    public Ad updateAds(int id, CreateOrUpdateAd createOrUpdateAd) {
        AdEntity adEntity = adRepo.findById((long)id).orElseThrow(() -> new EntityNotFoundException("Объявление с id=" + id + " не найдено"));
        adMapper.CreateOrUpdateAdToAdEntity(createOrUpdateAd, adEntity);
        return adMapper.adEntityToAd(adRepo.save(adEntity));
    }


}