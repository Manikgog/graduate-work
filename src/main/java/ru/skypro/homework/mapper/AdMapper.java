package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

@Mapper(componentModel = "spring")
public interface AdMapper {
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "pk", source = "id")
    Ad adEntityToAd(AdEntity source);

    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "phone", source = "author.phone")
    ExtendedAd adEntityToExtendedAd(AdEntity source);

    void createOrUpdateAdToAdEntity(CreateOrUpdateAd source, @MappingTarget AdEntity target);

}
