package io.ajl.nutriplus.external.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenFoodFactResponse(String code, OpenFoodFactProduct product) {

    public record OpenFoodFactProduct(
            String brands,
            @JsonProperty("generic_name_fr") String productName,
            @JsonProperty("image_front_url") String imageUrl,
            OpenFoodFactProductNutriments nutriments) {

        public record OpenFoodFactProductNutriments(
                @JsonProperty("calories") int calories,
                @JsonProperty("carbohydrates_100g") double carbs,
                @JsonProperty("sugars_100g") double sugars,
                @JsonProperty("proteins_100g") double proteins,
                @JsonProperty("fat_100g") double fat,
                @JsonProperty("saturated-fat_100g") double saturatedFat) {}
    }
}
