package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ad {
    @JsonProperty("author")
    @Schema(description = "id автора объявления", example = "0")
    Long author;
    @JsonProperty("image")
    @Schema(description = "ссылка на картинку объявления", example = "string")
    String image;
    @JsonProperty("pk")
    @Schema(description = "id объявления", example = "0")
    Long pk;
    @JsonProperty("price")
    @Schema(description = "цена объявления",example = "0")
    Double price;
    @JsonProperty("title")
    @Schema(description = "заголовок объявления", example = "string")
    String title;

}