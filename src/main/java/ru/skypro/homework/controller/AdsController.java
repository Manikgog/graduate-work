package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ads")
@CrossOrigin("http://localhost:3000")
public class AdsController {
    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @Operation(summary = "Получение всех объявлений", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class)))})
    @GetMapping
    public ResponseEntity<Ads> getAds() {
        log.info("The getAds method of AdsController is called");
        return ResponseEntity.ok().body(adsService.getAdAll());
    }




    @Operation(summary = "Добавление объявления", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Ad.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())})
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Ad> createAd(@RequestPart(value = "properties") @Valid CreateOrUpdateAd ad,
                                       @RequestPart(value = "image") MultipartFile image) {
        log.info("The createAd method of AdsController is called");
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.createAd(ad, image));
    }




    @Operation(summary = "Получение информации об объявлении", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedAd.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),

            @ApiResponse(responseCode = "404", description = "Not found", content = @Content())})
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAdsExtended(@PathVariable("id") Long id) {
        log.info("The getAdsExtended method of AdsController is called");
        return ResponseEntity.ok().body(adsService.getAd(id));
    }




    @Operation(summary = "Удаление объявления", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),

            @ApiResponse(responseCode = "401", description = "Unauthorized"),

            @ApiResponse(responseCode = "403", description = "Forbidden"),

            @ApiResponse(responseCode = "404", description = "Not found")})
    @PreAuthorize("@checkAccessService.isAdminOrOwnerAd(#id, authentication)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAds(@PathVariable("id") Long id) {
        log.info("The deleteAds method of AdsController is called");
        adsService.deleteAd(id);
        return ResponseEntity.ok().build();
    }




    @Operation(summary = "Обновление информации об объявлении", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),

            @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
    })
    @PreAuthorize("@checkAccessService.isAdminOrOwnerAd(#id, authentication)")
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable("id") Long id, @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return ResponseEntity.ok().body(adsService.updateAds(id, createOrUpdateAd));
    }




    @Operation(summary = "Получение объявлений авторизованного пользователя", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
    })
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsAuthorizedUser() {
        return ResponseEntity.ok().body(adsService.getAdsAuthorizedUser());
    }




    @Operation(summary = "Обновление картинки объявления", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/octet-stream",  array = @ArraySchema(schema = @Schema(implementation = byte[].class)))),

            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),

            @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
    })
    @PreAuthorize("@checkAccessService.isAdminOrOwnerAd(#id, authentication)")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> updateImage(@PathVariable("id") Long id,
                                                    @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok().body(adsService.updateImage(id, image));
    }



    @Operation(summary = "Получение фотографии товара по его id", tags = {"Объявления"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Получение изображения товара по его id прошло успешно",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
                            )
                    )
            }
    )
    @GetMapping(value = "/{id}/image")
    public ResponseEntity<?> getAdImage(@PathVariable("id") Long id, HttpServletResponse response) throws MalformedURLException {
        adsService.getImage(id, response);
        return ResponseEntity.ok().build();
    }
}