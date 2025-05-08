package io.ajl.nutriplus.testingutil;

import io.ajl.nutriplus.external.model.OpenFoodFactResponse;
import io.ajl.nutriplus.external.model.OpenFoodFactResponse.OpenFoodFactProduct;
import io.ajl.nutriplus.external.model.OpenFoodFactResponse.OpenFoodFactProduct.OpenFoodFactProductNutriments;
import io.ajl.nutriplus.service.model.Product;
import io.ajl.nutriplus.service.model.Product.Nutriments;
import io.ajl.nutriplus.util.Ean13;

public class DefaultObjects {

    private DefaultObjects() {}

    public static Ean13 defaultEan13() {
        return Ean13.of("3017620421006");
    }

    public static OpenFoodFactResponse defaultOpenFoodFactResponse() {
        var nutriments = new OpenFoodFactProductNutriments(150, 20, 5, 18, 1.3, 0.8);
        var product = new OpenFoodFactProduct("brands", "product", "https://img.url", nutriments);
        return new OpenFoodFactResponse("", product);
    }

    public static Product defaultProduct() {
        var nutriments = new Nutriments(150, 20, 5, 18, 1.3, 0.8);
        return new Product("ean", "name", "brand", nutriments);
    }
}
