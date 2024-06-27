package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.impl.AdsServiceImpl;

@RestController
@RequestMapping("/ads")
public class AdsController {
    private final AdsServiceImpl adsService;

    public AdsController(AdsServiceImpl adsService) {
        this.adsService = adsService;
    }

    @Operation(summary = "Получение всех объявлений", description = "", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class)))})
    @GetMapping
    public ResponseEntity<Ads> getAds() {
        return ResponseEntity.ok().body(new Ads());
    }

    @Operation(summary = "Добавление объявления", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Ad.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> createAd(@RequestPart(value="properties") CreateOrUpdateAd properties
            ,@RequestPart(value="image") MultipartFile image) {
        return ResponseEntity.ok().body(new Ad());
    }

    @Operation(summary = "Получение информации об объявлении", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedAd.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),

            @ApiResponse(responseCode = "404", description = "Not found", content = @Content())})
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAdsExtended(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(new ExtendedAd());
    }

    @Operation(summary = "Удаление объявления", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),

            @ApiResponse(responseCode = "401", description = "Unauthorized"),

            @ApiResponse(responseCode = "403", description = "Forbidden"),

            @ApiResponse(responseCode = "404", description = "Not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAds(@PathVariable("id") int id) {
        return ResponseEntity.ok().build();
    }
}
