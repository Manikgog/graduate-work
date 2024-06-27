package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ads {
    @JsonProperty("count")
    @Schema(description = "общее количество объявлений")
    int count;
    @JsonProperty("result")
    List<Ad> results;

    public Ads count(Integer count) {
        this.count = count;
        return this;
    }

    public Ads results(List<Ad> results) {
        this.results = results;
        return this;
    }

    public Ads addResultsItem(Ad resultsItem) {
        if (this.results == null) {
            this.results = new ArrayList<Ad>();
        }
        this.results.add(resultsItem);
        return this;
    }
}