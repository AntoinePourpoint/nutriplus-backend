package io.ajl.nutriplus.service.model;

public record Product (
        String ean,
        String name,
        String brand,
        Nutriments nutriments
) {

    public record Nutriments(
        int calories,
        double carbs,
        double sugars,
        double proteins,
        double fat,
        double saturatedFat
    ) {}
}
