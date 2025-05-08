package io.ajl.nutriplus.external;

import io.ajl.nutriplus.external.model.OpenFoodFactResponse;
import io.ajl.nutriplus.util.Ean13;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenFoodFactsApi {

  private static final String API_NAME = "openfoodfacts";
  private static final String PRODUCT_ENDPOINT = "product/%s";

  private final RestClientHelper restClientHelper;
  private final RestClient restClient;

  public OpenFoodFactsApi(RestClientHelper restClientHelper, RestClient restClient) {
    this.restClientHelper = restClientHelper;
    this.restClient = restClient;
  }

  public OpenFoodFactResponse scanProduct(Ean13 ean) {
    return restClient
        .get()
        .uri(restClientHelper.formatUri(API_NAME, PRODUCT_ENDPOINT).formatted(ean.getCode()))
        .retrieve()
        .body(new ParameterizedTypeReference<>() {});
  }
}
