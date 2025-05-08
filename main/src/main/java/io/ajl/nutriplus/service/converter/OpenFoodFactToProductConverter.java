package io.ajl.nutriplus.service.converter;

import io.ajl.nutriplus.external.model.OpenFoodFactResponse;
import io.ajl.nutriplus.external.model.OpenFoodFactResponse.OpenFoodFactProduct.OpenFoodFactProductNutriments;
import io.ajl.nutriplus.service.model.Product;
import io.ajl.nutriplus.service.model.Product.Nutriments;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OpenFoodFactToProductConverter implements Converter<OpenFoodFactResponse, Product> {
  @Override
  public Product convert(OpenFoodFactResponse source) {
    return new Product(
        source.code(),
        source.product().productName(),
        source.product().brands(),
        buildNutriments(source.product().nutriments()));
  }

  private Nutriments buildNutriments(OpenFoodFactProductNutriments source) {
    return new Nutriments(
        source.calories(),
        source.carbs(),
        source.sugars(),
        source.proteins(),
        source.fat(),
        source.saturatedFat());
  }
}
