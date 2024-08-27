package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdMapper {
    AdMapper INSTANCE = Mappers.getMapper( AdMapper.class );

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
